package cli;

import dto.TrainRunBookingView;
import exception.AppException;
import model.Route;
import model.RouteStop;
import model.Station;
import model.Train;
import model.TrainRun;
import repository.RouteRepository;
import repository.RouteStopRepository;
import repository.StationRepository;
import repository.TrainRepository;
import repository.TrainRunRepository;
import service.AdminService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AdminMenu {
    private final InputReader inputReader;
    private final ConsolePrinter printer;

    private final AdminService adminService;
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final TrainRunRepository trainRunRepository;

    public AdminMenu(InputReader inputReader, ConsolePrinter printer) {
        this.inputReader = inputReader;
        this.printer = printer;

        this.adminService = new AdminService();
        this.stationRepository = new StationRepository();
        this.trainRepository = new TrainRepository();
        this.routeRepository = new RouteRepository();
        this.routeStopRepository = new RouteStopRepository();
        this.trainRunRepository = new TrainRunRepository();
    }

    public void show() {
        boolean running = true;

        while (running) {
            printer.printHeader("ADMIN MENU");

            System.out.println("1. Manage stations");
            System.out.println("2. Manage trains");
            System.out.println("3. Manage routes");
            System.out.println("4. Show bookings for train run");
            System.out.println("5. Register delay and notify customers");
            System.out.println("0. Logout");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> manageStations();
                case 2 -> manageTrains();
                case 3 -> manageRoutes();
                case 4 -> showBookingsForTrainRun();
                case 5 -> registerDelayAndNotifyCustomers();
                case 0 -> {
                    System.out.println("Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void manageStations() {
        boolean running = true;

        while (running) {
            printer.printHeader("MANAGE STATIONS");

            System.out.println("1. List stations");
            System.out.println("2. Add station");
            System.out.println("3. Update station");
            System.out.println("4. Deactivate station");
            System.out.println("0. Back");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> listStations();
                case 2 -> addStation();
                case 3 -> updateStation();
                case 4 -> deactivateStation();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listStations() {
        printer.printHeader("STATIONS");

        List<Station> stations = stationRepository.findAll();

        for (Station station : stations) {
            System.out.println(
                    "ID: " + station.getId()
                            + " | " + station.getCode()
                            + " - " + station.getName()
                            + " (" + station.getCity() + ")"
                            + " | active: " + station.isActive()
            );
        }

        inputReader.pressEnterToContinue();
    }

    private void addStation() {
        printer.printHeader("ADD STATION");

        String code = inputReader.readString("Code: ");
        String name = inputReader.readString("Name: ");
        String city = inputReader.readString("City: ");

        try {
            Station station = adminService.addStation(code, name, city);
            printer.printSuccess("Station added with id: " + station.getId());
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void updateStation() {
        printer.printHeader("UPDATE STATION");

        listStationsWithoutPause();

        Long id = inputReader.readLong("Station id: ");
        String code = inputReader.readString("New code: ");
        String name = inputReader.readString("New name: ");
        String city = inputReader.readString("New city: ");
        boolean active = inputReader.readBoolean("Active");

        try {
            adminService.updateStation(id, code, name, city, active);
            printer.printSuccess("Station updated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void deactivateStation() {
        printer.printHeader("DEACTIVATE STATION");

        listStationsWithoutPause();

        Long id = inputReader.readLong("Station id: ");

        try {
            adminService.deactivateStation(id);
            printer.printSuccess("Station deactivated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void manageTrains() {
        boolean running = true;

        while (running) {
            printer.printHeader("MANAGE TRAINS");

            System.out.println("1. List trains");
            System.out.println("2. Add train");
            System.out.println("3. Update train");
            System.out.println("4. Deactivate train");
            System.out.println("0. Back");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> listTrains();
                case 2 -> addTrain();
                case 3 -> updateTrain();
                case 4 -> deactivateTrain();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listTrains() {
        printer.printHeader("TRAINS");

        List<Train> trains = trainRepository.findAll();

        for (Train train : trains) {
            System.out.println(
                    "ID: " + train.getId()
                            + " | " + train.getTrainNumber()
                            + " - " + train.getName()
                            + " | capacity: " + train.getCapacity()
                            + " | active: " + train.isActive()
            );
        }

        inputReader.pressEnterToContinue();
    }

    private void addTrain() {
        printer.printHeader("ADD TRAIN");

        String trainNumber = inputReader.readString("Train number: ");
        String name = inputReader.readString("Train name: ");
        int capacity = inputReader.readInt("Capacity: ");

        try {
            Train train = adminService.addTrain(trainNumber, name, capacity);
            printer.printSuccess("Train added with id: " + train.getId());
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void updateTrain() {
        printer.printHeader("UPDATE TRAIN");

        listTrainsWithoutPause();

        Long id = inputReader.readLong("Train id: ");
        String trainNumber = inputReader.readString("New train number: ");
        String name = inputReader.readString("New train name: ");
        int capacity = inputReader.readInt("New capacity: ");
        boolean active = inputReader.readBoolean("Active");

        try {
            adminService.updateTrain(id, trainNumber, name, capacity, active);
            printer.printSuccess("Train updated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void deactivateTrain() {
        printer.printHeader("DEACTIVATE TRAIN");

        listTrainsWithoutPause();

        Long id = inputReader.readLong("Train id: ");

        try {
            adminService.deactivateTrain(id);
            printer.printSuccess("Train deactivated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void manageRoutes() {
        boolean running = true;

        while (running) {
            printer.printHeader("MANAGE ROUTES");

            System.out.println("1. List routes with stops");
            System.out.println("2. Add route");
            System.out.println("3. Update route");
            System.out.println("4. Deactivate route");
            System.out.println("5. Add station to route");
            System.out.println("6. Update route stop");
            System.out.println("7. Remove station from route");
            System.out.println("0. Back");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> listRoutesWithStops();
                case 2 -> addRoute();
                case 3 -> updateRoute();
                case 4 -> deactivateRoute();
                case 5 -> addStationToRoute();
                case 6 -> updateRouteStop();
                case 7 -> removeStationFromRoute();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listRoutesWithStops() {
        printer.printHeader("ROUTES");

        List<Route> routes = routeRepository.findAll();

        for (Route route : routes) {
            System.out.println();
            System.out.println(
                    "Route ID: " + route.getId()
                            + " | " + route.getCode()
                            + " - " + route.getName()
                            + " | active: " + route.isActive()
            );

            List<RouteStop> stops = routeStopRepository.findByRouteId(route.getId());

            for (RouteStop stop : stops) {
                String stationText = stationRepository.findById(stop.getStationId())
                        .map(station -> station.getCode() + " - " + station.getName())
                        .orElse("station_id: " + stop.getStationId());

                System.out.println(
                        "   RouteStop ID: " + stop.getId()
                                + " | order: " + stop.getStopOrder()
                                + " | " + stationText
                                + " | km: " + stop.getDistanceFromStartKm()
                );
            }
        }

        inputReader.pressEnterToContinue();
    }

    private void addRoute() {
        printer.printHeader("ADD ROUTE");

        String code = inputReader.readString("Route code: ");
        String name = inputReader.readString("Route name: ");

        try {
            Route route = adminService.addRoute(code, name);
            printer.printSuccess("Route added with id: " + route.getId());
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void updateRoute() {
        printer.printHeader("UPDATE ROUTE");

        listRoutesWithoutPause();

        Long id = inputReader.readLong("Route id: ");
        String code = inputReader.readString("New route code: ");
        String name = inputReader.readString("New route name: ");
        boolean active = inputReader.readBoolean("Active");

        try {
            adminService.updateRoute(id, code, name, active);
            printer.printSuccess("Route updated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void deactivateRoute() {
        printer.printHeader("DEACTIVATE ROUTE");

        listRoutesWithoutPause();

        Long id = inputReader.readLong("Route id: ");

        try {
            adminService.deactivateRoute(id);
            printer.printSuccess("Route deactivated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void addStationToRoute() {
        printer.printHeader("ADD STATION TO ROUTE");

        listRoutesWithoutPause();
        listStationsWithoutPause();

        Long routeId = inputReader.readLong("Route id: ");
        Long stationId = inputReader.readLong("Station id: ");
        int stopOrder = inputReader.readInt("Stop order: ");
        BigDecimal km = inputReader.readBigDecimal("Distance from start km: ");

        try {
            RouteStop routeStop = adminService.addStationToRoute(routeId, stationId, stopOrder, km);
            printer.printSuccess("Station added to route. Route stop id: " + routeStop.getId());
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void updateRouteStop() {
        printer.printHeader("UPDATE ROUTE STOP");

        listRoutesWithStopsWithoutPause();
        listStationsWithoutPause();

        Long routeStopId = inputReader.readLong("Route stop id: ");
        Long routeId = inputReader.readLong("Route id: ");
        Long stationId = inputReader.readLong("Station id: ");
        int stopOrder = inputReader.readInt("Stop order: ");
        BigDecimal km = inputReader.readBigDecimal("Distance from start km: ");

        try {
            adminService.updateRouteStop(routeStopId, routeId, stationId, stopOrder, km);
            printer.printSuccess("Route stop updated.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void removeStationFromRoute() {
        printer.printHeader("REMOVE STATION FROM ROUTE");

        listRoutesWithStopsWithoutPause();

        Long routeStopId = inputReader.readLong("Route stop id: ");

        try {
            adminService.removeStationFromRoute(routeStopId);
            printer.printSuccess("Station removed from route.");
        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void showBookingsForTrainRun() {
        printer.printHeader("SHOW BOOKINGS FOR TRAIN RUN");

        listTrainRunsWithoutPause();

        Long trainRunId = inputReader.readLong("Train run id: ");

        try {
            List<TrainRunBookingView> bookings = adminService.showBookingsForTrainRun(trainRunId);

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
                        "Route: "
                                + booking.getOriginStationCode() + " " + booking.getOriginStationName()
                                + " -> "
                                + booking.getDestinationStationCode() + " " + booking.getDestinationStationName()
                );
                System.out.println("Departure: " + booking.getDepartureTime());
                System.out.println("Arrival:   " + booking.getArrivalTime());
            }

        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void registerDelayAndNotifyCustomers() {
        printer.printHeader("REGISTER DELAY AND NOTIFY CUSTOMERS");

        listTrainRunsWithoutPause();

        Long trainRunId = inputReader.readLong("Train run id: ");
        int delayMinutes = inputReader.readInt("Delay minutes: ");
        String reason = inputReader.readString("Reason: ");

        try {
            int notifiedCustomers = adminService.registerDelayAndNotifyCustomers(
                    trainRunId,
                    delayMinutes,
                    reason
            );

            printer.printSuccess("Delay registered.");
            System.out.println("Customers notified: " + notifiedCustomers);

        } catch (AppException e) {
            printer.printError(e.getMessage());
        }

        inputReader.pressEnterToContinue();
    }

    private void listStationsWithoutPause() {
        System.out.println();
        System.out.println("Stations:");

        List<Station> stations = stationRepository.findAll();

        for (Station station : stations) {
            System.out.println(
                    "ID: " + station.getId()
                            + " | " + station.getCode()
                            + " - " + station.getName()
                            + " | active: " + station.isActive()
            );
        }
    }

    private void listTrainsWithoutPause() {
        System.out.println();
        System.out.println("Trains:");

        List<Train> trains = trainRepository.findAll();

        for (Train train : trains) {
            System.out.println(
                    "ID: " + train.getId()
                            + " | " + train.getTrainNumber()
                            + " - " + train.getName()
                            + " | capacity: " + train.getCapacity()
                            + " | active: " + train.isActive()
            );
        }
    }

    private void listRoutesWithoutPause() {
        System.out.println();
        System.out.println("Routes:");

        List<Route> routes = routeRepository.findAll();

        for (Route route : routes) {
            System.out.println(
                    "ID: " + route.getId()
                            + " | " + route.getCode()
                            + " - " + route.getName()
                            + " | active: " + route.isActive()
            );
        }
    }

    private void listRoutesWithStopsWithoutPause() {
        System.out.println();
        System.out.println("Routes with stops:");

        List<Route> routes = routeRepository.findAll();

        for (Route route : routes) {
            System.out.println();
            System.out.println(
                    "Route ID: " + route.getId()
                            + " | " + route.getCode()
                            + " - " + route.getName()
                            + " | active: " + route.isActive()
            );

            List<RouteStop> stops = routeStopRepository.findByRouteId(route.getId());

            for (RouteStop stop : stops) {
                String stationText = stationRepository.findById(stop.getStationId())
                        .map(station -> station.getCode() + " - " + station.getName())
                        .orElse("station_id: " + stop.getStationId());

                System.out.println(
                        "   RouteStop ID: " + stop.getId()
                                + " | order: " + stop.getStopOrder()
                                + " | " + stationText
                                + " | km: " + stop.getDistanceFromStartKm()
                );
            }
        }
    }

    private void listTrainRunsWithoutPause() {
        System.out.println();
        System.out.println("Train runs:");

        List<TrainRun> trainRuns = trainRunRepository.findAll();

        for (TrainRun trainRun : trainRuns) {
            String trainText = trainRepository.findById(trainRun.getTrainId())
                    .map(train -> train.getTrainNumber() + " - " + train.getName())
                    .orElse("train_id: " + trainRun.getTrainId());

            System.out.println(
                    "ID: " + trainRun.getId()
                            + " | " + trainRun.getRunCode()
                            + " | " + trainText
                            + " | date: " + trainRun.getServiceDate()
                            + " | status: " + trainRun.getStatus()
                            + " | delay: " + trainRun.getDelayMinutes() + " min"
            );
        }
    }
}