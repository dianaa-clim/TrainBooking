package cli;

import dto.*;
import exception.AppException;
import service.BookingService;
import service.SearchService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerMenu {
    private final InputReader inputReader;
    private final ConsolePrinter printer;
    private final SearchService searchService;
    private final BookingService bookingService;
    private final AuthenticatedUser currentUser;

    private List<JourneyOption> lastSearchOptions = new ArrayList<>();

    public CustomerMenu(InputReader inputReader, ConsolePrinter printer) {
        this(inputReader, printer, null);
    }

    public CustomerMenu(InputReader inputReader, ConsolePrinter printer, AuthenticatedUser currentUser) {
        this.inputReader = inputReader;
        this.printer = printer;
        this.currentUser = currentUser;
        this.searchService = new SearchService();
        this.bookingService = new BookingService();
    }

    public void show() {
        boolean running = true;

        while (running) {
            printer.printHeader("CUSTOMER MENU");

            System.out.println("1. Search journey");
            System.out.println("2. Book tickets from last search");
            System.out.println("0. Logout");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> searchJourney();
                case 2 -> bookTickets();
                case 0 -> {
                    System.out.println("Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void searchJourney() {
        printer.printHeader("SEARCH JOURNEY");

        String departureCode = inputReader.readString("Departure station code: ");
        String arrivalCode = inputReader.readString("Arrival station code: ");
        LocalDate travelDate = inputReader.readDate("Travel date (YYYY-MM-DD): ");

        try {
            lastSearchOptions = searchService.searchJourneys(
                    departureCode,
                    arrivalCode,
                    travelDate
            );

            System.out.println();
            System.out.println("Journey options found:");

            for (JourneyOption option : lastSearchOptions) {
                printJourneyOption(option);
            }

        } catch (AppException e) {
            printer.printError(e.getMessage());
            lastSearchOptions = new ArrayList<>();
        }

        inputReader.pressEnterToContinue();
    }

    private void bookTickets() {
        printer.printHeader("BOOK TICKETS");

        if (lastSearchOptions == null || lastSearchOptions.isEmpty()) {
            System.out.println("No journey search found. Please search a journey first.");
            inputReader.pressEnterToContinue();
            return;
        }

        System.out.println("Available options from last search:");

        for (JourneyOption option : lastSearchOptions) {
            printJourneyOptionShort(option);
        }

        int selectedOptionNumber = inputReader.readInt("Choose journey option number: ");

        JourneyOption selectedOption = findOptionByNumber(selectedOptionNumber);

        if (selectedOption == null) {
            printer.printError("Invalid journey option number.");
            inputReader.pressEnterToContinue();
            return;
        }

        String customerName;
        String customerEmail;

        if (currentUser != null) {
            customerName = currentUser.getFullName();
            customerEmail = currentUser.getEmail();

            System.out.println("Booking will be made for:");
            System.out.println(customerName + " <" + customerEmail + ">");
        } else {
            customerName = inputReader.readString("Customer full name: ");
            customerEmail = inputReader.readString("Customer email: ");
        }

        int passengerCount = inputReader.readInt("Number of passengers: ");

        List<PassengerRequest> passengers = new ArrayList<>();

        for (int i = 1; i <= passengerCount; i++) {
            String passengerName = inputReader.readString("Passenger " + i + " full name: ");
            passengers.add(new PassengerRequest(passengerName));
        }

        BookingRequest bookingRequest = new BookingRequest(
                customerName,
                customerEmail,
                selectedOption,
                passengers
        );

        try {
            BookingResult result = bookingService.bookTickets(bookingRequest);

            printer.printSuccess("Booking confirmed.");
            System.out.println("Booking code: " + result.getBookingCode());

            System.out.println();
            System.out.println("Tickets:");

            for (TicketDetails ticket : result.getTicketDetails()) {
                printTicket(ticket);
            }

        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private JourneyOption findOptionByNumber(int optionNumber) {
        for (JourneyOption option : lastSearchOptions) {
            if (option.getOptionNumber() == optionNumber) {
                return option;
            }
        }

        return null;
    }

    private void printJourneyOption(JourneyOption option) {
        System.out.println();
        System.out.println("Option " + option.getOptionNumber()
                + (option.isDirect() ? " - Direct" : " - 1 change"));

        System.out.println("Total duration: " + option.getTotalDurationMinutes() + " minutes");

        for (JourneyLeg leg : option.getLegs()) {
            System.out.println(
                    "Leg " + leg.getLegOrder() + ": " +
                            leg.getTrainNumber() + " - " + leg.getTrainName()
            );

            System.out.println(
                    "   " + leg.getOriginStationCode() + " " + leg.getOriginStationName()
                            + " -> "
                            + leg.getDestinationStationCode() + " " + leg.getDestinationStationName()
            );

            System.out.println("   Departure: " + leg.getDepartureTime());
            System.out.println("   Arrival:   " + leg.getArrivalTime());

            if (leg.getDelayMinutes() > 0) {
                System.out.println("   Delay: " + leg.getDelayMinutes() + " minutes");
            }
        }
    }

    private void printJourneyOptionShort(JourneyOption option) {
        JourneyLeg firstLeg = option.getLegs().getFirst();
        JourneyLeg lastLeg = option.getLegs().getLast();

        System.out.println();
        System.out.println("Option " + option.getOptionNumber()
                + (option.isDirect() ? " - Direct" : " - 1 change"));

        System.out.println(
                firstLeg.getOriginStationCode() +
                        " -> " +
                        lastLeg.getDestinationStationCode() +
                        " | departure: " + option.getDepartureTime() +
                        " | arrival: " + option.getArrivalTime() +
                        " | duration: " + option.getTotalDurationMinutes() + " minutes"
        );
    }

    private void printTicket(TicketDetails ticket) {
        System.out.println();
        System.out.println("Ticket: " + ticket.getTicketCode());
        System.out.println("Passenger: " + ticket.getPassengerName());
        System.out.println("Train: " + ticket.getTrainNumber() + " - " + ticket.getTrainName());

        System.out.println(
                "Route: " +
                        ticket.getOriginStationCode() + " " + ticket.getOriginStationName() +
                        " -> " +
                        ticket.getDestinationStationCode() + " " + ticket.getDestinationStationName()
        );

        System.out.println("Departure: " + ticket.getDepartureTime());
        System.out.println("Arrival:   " + ticket.getArrivalTime());
    }
}