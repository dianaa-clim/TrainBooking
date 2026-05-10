package com.trainbooking.train_booking_backend.booking;

import com.trainbooking.train_booking_backend.booking.dto.BookingLegRequest;
import com.trainbooking.train_booking_backend.booking.dto.BookingRequest;
import com.trainbooking.train_booking_backend.booking.dto.BookingResponse;
import com.trainbooking.train_booking_backend.customer.Customer;
import com.trainbooking.train_booking_backend.customer.CustomerRepository;
import com.trainbooking.train_booking_backend.email.EmailService;
import com.trainbooking.train_booking_backend.exception.BadRequestException;
import com.trainbooking.train_booking_backend.exception.OverbookingException;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.run.TrainRun;
import com.trainbooking.train_booking_backend.run.TrainRunRepository;
import com.trainbooking.train_booking_backend.run.TrainRunStop;
import com.trainbooking.train_booking_backend.run.TrainRunStopRepository;
import com.trainbooking.train_booking_backend.booking.mapper.BookingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingLegRepository bookingLegRepository;
    private final CustomerRepository customerRepository;
    private final TrainRunRepository trainRunRepository;
    private final TrainRunStopRepository trainRunStopRepository;
    private final BookingMapper bookingMapper;
    private final EmailService emailService;

    public BookingService(
            BookingRepository bookingRepository,
            BookingLegRepository bookingLegRepository,
            CustomerRepository customerRepository,
            TrainRunRepository trainRunRepository,
            TrainRunStopRepository trainRunStopRepository,
            BookingMapper bookingMapper,
            EmailService emailService
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingLegRepository = bookingLegRepository;
        this.customerRepository = customerRepository;
        this.trainRunRepository = trainRunRepository;
        this.trainRunStopRepository = trainRunStopRepository;
        this.bookingMapper = bookingMapper;
        this.emailService = emailService;
    }

    @Transactional
    public BookingResponse createBooking(String customerEmail, BookingRequest request) {
        Customer customer = findCustomerByEmail(customerEmail);

        validateBookingRequest(request);
        validateLegSequence(request.getLegs());

        int requestedSeats = request.getPassengers().size();

        for (BookingLegRequest legRequest : request.getLegs()) {
            TrainRun trainRun = findTrainRunById(legRequest.getTrainRunId());
            TrainRunStop originStop = findTrainRunStopById(legRequest.getOriginStopId());
            TrainRunStop destinationStop = findTrainRunStopById(legRequest.getDestinationStopId());

            validateLeg(trainRun, originStop, destinationStop);
            validateCapacityForLeg(trainRun, originStop, destinationStop, requestedSeats);
        }

        Booking booking = new Booking();
        booking.setBookingCode(generateUniqueBookingCode());
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.CONFIRMED);

        request.getPassengers().forEach(passengerRequest -> {
            BookingPassenger passenger = new BookingPassenger();
            passenger.setBooking(booking);
            passenger.setFullName(passengerRequest.getFullName().trim());
            booking.getPassengers().add(passenger);
        });

        for (int i = 0; i < request.getLegs().size(); i++) {
            BookingLegRequest legRequest = request.getLegs().get(i);

            TrainRun trainRun = findTrainRunById(legRequest.getTrainRunId());
            TrainRunStop originStop = findTrainRunStopById(legRequest.getOriginStopId());
            TrainRunStop destinationStop = findTrainRunStopById(legRequest.getDestinationStopId());

            BookingLeg bookingLeg = new BookingLeg();
            bookingLeg.setBooking(booking);
            bookingLeg.setTrainRun(trainRun);
            bookingLeg.setOriginStop(originStop);
            bookingLeg.setDestinationStop(destinationStop);
            bookingLeg.setLegOrder(i + 1);

            booking.getLegs().add(bookingLeg);
        }

        for (BookingPassenger passenger : booking.getPassengers()) {
            for (BookingLeg leg : booking.getLegs()) {
                Ticket ticket = new Ticket();
                ticket.setTicketCode(generateUniqueTicketCode());
                ticket.setBooking(booking);
                ticket.setPassenger(passenger);
                ticket.setLeg(leg);

                booking.getTickets().add(ticket);
            }
        }

        Booking savedBooking = bookingRepository.save(booking);
        emailService.sendBookingConfirmation(savedBooking);

        return bookingMapper.toResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);

        return bookingRepository.findByCustomerOrderByCreatedAtDesc(customer)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getMyBookingByCode(String customerEmail, String bookingCode) {
        Customer customer = findCustomerByEmail(customerEmail);

        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with code: " + bookingCode));

        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new ResourceNotFoundException("Booking not found with code: " + bookingCode);
        }

        return bookingMapper.toResponse(booking);
    }

    private void validateBookingRequest(BookingRequest request) {
        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new BadRequestException("At least one passenger is required.");
        }

        if (request.getLegs() == null || request.getLegs().isEmpty()) {
            throw new BadRequestException("At least one booking leg is required.");
        }

        if (request.getLegs().size() > 2) {
            throw new BadRequestException("A booking can contain maximum two legs.");
        }
    }

    private void validateLegSequence(List<BookingLegRequest> legs) {
        if (legs.size() == 1) {
            return;
        }

        BookingLegRequest firstLegRequest = legs.get(0);
        BookingLegRequest secondLegRequest = legs.get(1);

        TrainRunStop firstDestination = findTrainRunStopById(firstLegRequest.getDestinationStopId());
        TrainRunStop secondOrigin = findTrainRunStopById(secondLegRequest.getOriginStopId());

        if (!firstDestination.getStation().getId().equals(secondOrigin.getStation().getId())) {
            throw new BadRequestException("For a one-change journey, the destination of the first leg must be the origin of the second leg.");
        }

        if (firstDestination.getPlannedArrivalTime() == null || secondOrigin.getPlannedDepartureTime() == null) {
            throw new BadRequestException("Invalid transfer times.");
        }

        if (secondOrigin.getPlannedDepartureTime().isBefore(firstDestination.getPlannedArrivalTime())) {
            throw new BadRequestException("The second train must depart after the first train arrives.");
        }
    }

    private void validateLeg(
            TrainRun trainRun,
            TrainRunStop originStop,
            TrainRunStop destinationStop
    ) {
        if (!trainRun.isActive()) {
            throw new BadRequestException("Cannot book tickets on an inactive train run.");
        }

        if (!originStop.getTrainRun().getId().equals(trainRun.getId())) {
            throw new BadRequestException("Origin stop does not belong to the selected train run.");
        }

        if (!destinationStop.getTrainRun().getId().equals(trainRun.getId())) {
            throw new BadRequestException("Destination stop does not belong to the selected train run.");
        }

        if (originStop.getStopOrder() >= destinationStop.getStopOrder()) {
            throw new BadRequestException("Destination stop must be after origin stop.");
        }
    }

    private void validateCapacityForLeg(
            TrainRun trainRun,
            TrainRunStop requestedOrigin,
            TrainRunStop requestedDestination,
            int requestedSeats
    ) {
        int capacity = trainRun.getTrain().getCapacity();

        int requestedOriginOrder = requestedOrigin.getStopOrder();
        int requestedDestinationOrder = requestedDestination.getStopOrder();

        List<BookingLeg> existingLegs = bookingLegRepository.findByTrainRunId(trainRun.getId());

        for (int segmentStart = requestedOriginOrder; segmentStart < requestedDestinationOrder; segmentStart++) {
            int segmentEnd = segmentStart + 1;

            int occupiedSeats = 0;

            for (BookingLeg existingLeg : existingLegs) {
                if (existingLeg.getBooking().getStatus() != BookingStatus.CONFIRMED) {
                    continue;
                }

                int existingOriginOrder = existingLeg.getOriginStop().getStopOrder();
                int existingDestinationOrder = existingLeg.getDestinationStop().getStopOrder();

                boolean overlaps =
                        existingOriginOrder < segmentEnd
                                && existingDestinationOrder > segmentStart;

                if (overlaps) {
                    occupiedSeats += existingLeg.getBooking().getPassengers().size();
                }
            }

            if (occupiedSeats + requestedSeats > capacity) {
                throw new OverbookingException(
                        "Not enough seats available on segment "
                                + segmentStart
                                + " -> "
                                + segmentEnd
                                + ". Available seats: "
                                + (capacity - occupiedSeats)
                                + ", requested seats: "
                                + requestedSeats
                );
            }
        }
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
    }

    private TrainRun findTrainRunById(Long id) {
        return trainRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train run not found with id: " + id));
    }

    private TrainRunStop findTrainRunStopById(Long id) {
        return trainRunStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train run stop not found with id: " + id));
    }

    private String generateUniqueBookingCode() {
        String code;

        do {
            code = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (bookingRepository.existsByBookingCode(code));

        return code;
    }

    private String generateUniqueTicketCode() {
        return "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsForTrainRun(Long trainRunId) {
        findTrainRunById(trainRunId);

        List<BookingLeg> bookingLegs = bookingLegRepository.findByTrainRunId(trainRunId);

        Map<Long, Booking> uniqueBookings = new LinkedHashMap<>();

        for (BookingLeg leg : bookingLegs) {
            Booking booking = leg.getBooking();

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                uniqueBookings.put(booking.getId(), booking);
            }
        }

        return uniqueBookings.values()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}