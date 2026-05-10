package service;

import config.ConnectionFactory;
import dto.*;
import exception.DatabaseException;
import exception.ValidationException;
import model.*;
import repository.*;
import util.CodeGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final AvailabilityService availabilityService;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final BookingLegRepository bookingLegRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    public BookingService() {
        this.availabilityService = new AvailabilityService();
        this.customerRepository = new CustomerRepository();
        this.bookingRepository = new BookingRepository();
        this.bookingPassengerRepository = new BookingPassengerRepository();
        this.bookingLegRepository = new BookingLegRepository();
        this.ticketRepository = new TicketRepository();
        this.emailService = new SmtpEmailService();
    }

    public BookingResult bookTickets(BookingRequest request) {
        validateRequest(request);

        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);

                int passengerCount = request.getPassengers().size();

                availabilityService.ensureAvailable(
                        connection,
                        request.getJourneyOption(),
                        passengerCount
                );

                Customer customer = findOrCreateCustomer(connection, request);

                Booking booking = new Booking(
                        null,
                        CodeGenerator.generateBookingCode(),
                        customer.getId(),
                        BookingStatus.CONFIRMED,
                        null
                );

                bookingRepository.save(connection, booking);

                List<BookingPassenger> savedPassengers = savePassengers(
                        connection,
                        booking.getId(),
                        request.getPassengers()
                );

                List<TicketDetails> ticketDetails = saveBookingLegsAndTickets(
                        connection,
                        booking.getId(),
                        request.getJourneyOption().getLegs(),
                        savedPassengers,
                        passengerCount
                );

                List<String> ticketCodes = ticketDetails.stream()
                        .map(TicketDetails::getTicketCode)
                        .toList();

                emailService.sendBookingConfirmation(connection, booking, customer, ticketDetails);

                connection.commit();

                return new BookingResult(booking.getBookingCode(), ticketCodes, ticketDetails);

            } catch (RuntimeException e) {
                rollback(connection);
                throw e;
            } catch (SQLException e) {
                rollback(connection);
                throw new DatabaseException("Could not complete booking transaction.", e);
            } finally {
                restoreAutoCommit(connection);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not open database connection for booking.", e);
        }
    }

    private Customer findOrCreateCustomer(Connection connection, BookingRequest request) {
        return customerRepository.findByEmail(connection, request.getCustomerEmail().trim().toLowerCase())
                .orElseGet(() -> {
                    Customer customer = new Customer(
                            null,
                            request.getCustomerName().trim(),
                            request.getCustomerEmail().trim().toLowerCase(),
                            null
                    );

                    return customerRepository.save(connection, customer);
                });
    }

    private List<BookingPassenger> savePassengers(Connection connection, Long bookingId,
                                                  List<PassengerRequest> passengerRequests) {
        List<BookingPassenger> savedPassengers = new ArrayList<>();

        for (PassengerRequest passengerRequest : passengerRequests) {
            BookingPassenger passenger = new BookingPassenger(
                    null,
                    bookingId,
                    passengerRequest.getFullName().trim()
            );

            bookingPassengerRepository.save(connection, passenger);
            savedPassengers.add(passenger);
        }

        return savedPassengers;
    }

    private List<TicketDetails> saveBookingLegsAndTickets(Connection connection, Long bookingId,
                                                          List<JourneyLeg> journeyLegs,
                                                          List<BookingPassenger> savedPassengers,
                                                          int passengerCount) {
        List<TicketDetails> ticketDetails = new ArrayList<>();

        for (JourneyLeg journeyLeg : journeyLegs) {
            BookingLeg bookingLeg = new BookingLeg(
                    null,
                    bookingId,
                    journeyLeg.getTrainRunId(),
                    journeyLeg.getOriginRunStopId(),
                    journeyLeg.getDestinationRunStopId(),
                    journeyLeg.getLegOrder(),
                    passengerCount,
                    BookingStatus.CONFIRMED
            );

            bookingLegRepository.save(connection, bookingLeg);

            for (BookingPassenger passenger : savedPassengers) {
                String ticketCode = CodeGenerator.generateTicketCode();

                Ticket ticket = new Ticket(
                        null,
                        ticketCode,
                        bookingLeg.getId(),
                        passenger.getId(),
                        TicketStatus.VALID,
                        null
                );

                ticketRepository.save(connection, ticket);

                TicketDetails details = new TicketDetails(
                        ticketCode,
                        passenger.getFullName(),
                        journeyLeg.getTrainNumber(),
                        journeyLeg.getTrainName(),
                        journeyLeg.getOriginStationCode(),
                        journeyLeg.getOriginStationName(),
                        journeyLeg.getDestinationStationCode(),
                        journeyLeg.getDestinationStationName(),
                        journeyLeg.getDepartureTime(),
                        journeyLeg.getArrivalTime()
                );

                ticketDetails.add(details);
            }
        }

        return ticketDetails;
    }
    private void validateRequest(BookingRequest request) {
        if (request == null) {
            throw new ValidationException("Booking request cannot be null.");
        }

        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            throw new ValidationException("Customer name is required.");
        }

        if (request.getCustomerEmail() == null || request.getCustomerEmail().isBlank()) {
            throw new ValidationException("Customer email is required.");
        }

        if (!request.getCustomerEmail().contains("@")) {
            throw new ValidationException("Customer email is invalid.");
        }

        if (request.getJourneyOption() == null
                || request.getJourneyOption().getLegs() == null
                || request.getJourneyOption().getLegs().isEmpty()) {
            throw new ValidationException("Journey option is required.");
        }

        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new ValidationException("At least one passenger is required.");
        }

        for (PassengerRequest passenger : request.getPassengers()) {
            if (passenger.getFullName() == null || passenger.getFullName().isBlank()) {
                throw new ValidationException("Passenger name is required.");
            }
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseException("Could not rollback booking transaction.", e);
        }
    }

    private void restoreAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseException("Could not restore auto commit.", e);
        }
    }
}