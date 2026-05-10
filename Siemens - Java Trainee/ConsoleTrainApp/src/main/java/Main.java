import cli.ConsoleApp;
import config.ConnectionFactory;
import dto.*;
import exception.AppException;
import exception.DatabaseException;
import model.*;
import repository.*;
import service.AdminService;
import service.AvailabilityService;
import service.BookingService;
import service.SearchService;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Console Train Booking App...");

        try (Connection connection = ConnectionFactory.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful.");
            }
        } catch (DatabaseException e) {
            System.out.println("Database connection test failed.");
            System.out.println(e.getMessage());

            if (e.getCause() != null) {
                System.out.println("Real cause:");
                e.getCause().printStackTrace();
            }

            return;
        } catch (Exception e) {
            System.out.println("Unexpected error.");
            e.printStackTrace();
            return;
        }

        ConsoleApp app = new ConsoleApp();
        app.start();

        /*StationRepository stationRepository = new StationRepository();
        TrainRepository trainRepository = new TrainRepository();

        List<Station> stations = stationRepository.findAllActive();

        System.out.println("\nAvailable stations:");

        for (Station station : stations) {
            System.out.println(
                    station.getCode() + " - " +
                            station.getName() + " (" +
                            station.getCity() + ")"
            );
        }

        List<Train> trains = trainRepository.findAllActive();

        System.out.println("\nAvailable trains:");

        for (Train train : trains) {
            System.out.println(
                    train.getTrainNumber() + " - " +
                            train.getName() +
                            " | capacity: " + train.getCapacity()
            );
        }

        RouteRepository routeRepository = new RouteRepository();
        RouteStopRepository routeStopRepository = new RouteStopRepository();

        List<Route> routes = routeRepository.findAllActive();

        System.out.println("\nAvailable routes:");

        for (Route route : routes) {
            System.out.println(route.getCode() + " - " + route.getName());

            List<RouteStop> routeStops = routeStopRepository.findByRouteId(route.getId());

            for (RouteStop routeStop : routeStops) {
                System.out.println(
                        "   stop " + routeStop.getStopOrder() +
                                " | station_id: " + routeStop.getStationId() +
                                " | km: " + routeStop.getDistanceFromStartKm()
                );
            }
        }

        TrainRunRepository trainRunRepository = new TrainRunRepository();
        TrainRunStopRepository trainRunStopRepository = new TrainRunStopRepository();

        List<TrainRun> trainRuns = trainRunRepository.findByServiceDate(LocalDate.of(2026, 5, 10));

        System.out.println("\nTrain runs on 2026-05-10:");

        for (TrainRun trainRun : trainRuns) {
            System.out.println(
                    trainRun.getRunCode() +
                            " | train_id: " + trainRun.getTrainId() +
                            " | route_id: " + trainRun.getRouteId() +
                            " | status: " + trainRun.getStatus()
            );

            List<TrainRunStop> stops = trainRunStopRepository.findByTrainRunId(trainRun.getId());

            for (TrainRunStop stop : stops) {
                System.out.println(
                        "   stop " + stop.getStopOrder() +
                                " | station_id: " + stop.getStationId() +
                                " | arrival: " + stop.getPlannedArrival() +
                                " | departure: " + stop.getPlannedDeparture()
                );
            }
        }*/

        //testSearchService();
        //testBookingService();
        //testOverbookingProtection();
        //testAdminServiceBasic();
        //testAdminBookingsAndDelay();
    }

    private static void testSearchService() {
        System.out.println("SEARCH SERVICE TEST");

        SearchService searchService = new SearchService();

        try {
            List<JourneyOption> options = searchService.searchJourneys(
                    "CLJ",
                    "BUC",
                    LocalDate.of(2026, 5, 10)
            );

            System.out.println("\nJourney options found for CLJ -> BUC on 2026-05-10:");

            for (JourneyOption option : options) {
                printJourneyOption(option);
            }

        } catch (AppException e) {
            System.out.println("Search failed:");
            System.out.println(e.getMessage());
        }
    }

    private static void testBookingService() {
        System.out.println("BOOKING SERVICE TEST");

        SearchService searchService = new SearchService();
        BookingService bookingService = new BookingService();

        try {
            List<JourneyOption> options = searchService.searchJourneys(
                    "CLJ",
                    "BUC",
                    LocalDate.of(2026, 5, 10)
            );

            JourneyOption selectedOption = options.stream()
                    .filter(JourneyOption::isDirect)
                    .findFirst()
                    .orElse(options.getFirst());

            BookingRequest bookingRequest = new BookingRequest(
                    "Popescu Ana",
                    "ana.popescu@test.com",
                    selectedOption,
                    List.of(
                            new PassengerRequest("Popescu Ana"),
                            new PassengerRequest("Ionescu Mihai")
                    )
            );

            BookingResult result = bookingService.bookTickets(bookingRequest);

            System.out.println("Booking confirmed.");
            System.out.println("Booking code: " + result.getBookingCode());

            System.out.println("Tickets:");

            for (TicketDetails ticket : result.getTicketDetails()) {
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

        } catch (AppException e) {
            System.out.println("Booking failed:");
            System.out.println(e.getMessage());
        }
    }

    private static void testOverbookingProtection() {
        System.out.println("OVERBOOKING TEST");

        SearchService searchService = new SearchService();
        AvailabilityService availabilityService = new AvailabilityService();

        try (Connection connection = ConnectionFactory.getConnection()) {
            List<JourneyOption> options = searchService.searchJourneys(
                    "CLJ",
                    "BUC",
                    LocalDate.of(2026, 5, 10)
            );

            JourneyOption directOption = options.stream()
                    .filter(JourneyOption::isDirect)
                    .findFirst()
                    .orElse(options.getFirst());

            int requestedSeats = 121;

            AvailabilityResult result = availabilityService.checkAvailability(
                    connection,
                    directOption,
                    requestedSeats
            );

            System.out.println("Requested seats: " + requestedSeats);
            System.out.println("Available: " + result.isAvailable());
            System.out.println("Minimum available seats: " + result.getMinimumAvailableSeats());
            System.out.println("Message: " + result.getMessage());

        } catch (Exception e) {
            System.out.println("Overbooking test failed:");
            e.printStackTrace();
        }
    }

    private static void testAdminServiceBasic() {
        System.out.println("ADMIN SERVICE BASIC TEST");

        AdminService adminService = new AdminService();

        try {
            Train train = adminService.addTrain(
                    "TEST100",
                    "Test Train Admin",
                    50
            );

            System.out.println("Train added:");
            System.out.println(train.getTrainNumber() + " - " + train.getName() + " | capacity: " + train.getCapacity());

            adminService.updateTrain(
                    train.getId(),
                    "TEST100",
                    "Updated Test Train Admin",
                    60,
                    true
            );

            System.out.println("Train updated.");

            adminService.deactivateTrain(train.getId());

            System.out.println("Train deactivated.");

        } catch (AppException e) {
            System.out.println("Admin test failed:");
            System.out.println(e.getMessage());
        }
    }

    private static void testAdminBookingsAndDelay() {
        System.out.println("ADMIN BOOKINGS + DELAY TEST");

        AdminService adminService = new AdminService();
        TrainRunRepository trainRunRepository = new TrainRunRepository();

        try {
            Optional<TrainRun> trainRunOptional =
                    trainRunRepository.findByRunCode("RUN-IR1745-2026-05-10");

            if (trainRunOptional.isEmpty()) {
                System.out.println("Train run not found.");
                return;
            }

            TrainRun trainRun = trainRunOptional.get();

            List<TrainRunBookingView> bookings =
                    adminService.showBookingsForTrainRun(trainRun.getId());

            System.out.println("Bookings for train run " + trainRun.getRunCode() + ":");

            if (bookings.isEmpty()) {
                System.out.println("No bookings found for this train run.");
            }

            for (TrainRunBookingView booking : bookings) {
                System.out.println();
                System.out.println("Booking: " + booking.getBookingCode());
                System.out.println("Customer: " + booking.getCustomerName() + " <" + booking.getCustomerEmail() + ">");
                System.out.println("Passenger: " + booking.getPassengerName());
                System.out.println("Ticket: " + booking.getTicketCode());
                System.out.println(
                        "Route: " +
                                booking.getOriginStationCode() + " " + booking.getOriginStationName() +
                                " -> " +
                                booking.getDestinationStationCode() + " " + booking.getDestinationStationName()
                );
                System.out.println("Departure: " + booking.getDepartureTime());
                System.out.println("Arrival:   " + booking.getArrivalTime());
            }

            int notifiedCustomers = adminService.registerDelayAndNotifyCustomers(
                    trainRun.getId(),
                    25,
                    "Operational delay test"
            );

            System.out.println();
            System.out.println("Delay registered.");
            System.out.println("Customers notified: " + notifiedCustomers);

        } catch (AppException e) {
            System.out.println("Admin bookings/delay test failed:");
            System.out.println(e.getMessage());
        }
    }

    private static void printJourneyOption(JourneyOption option) {
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
}