package service;

import dto.JourneyLeg;
import dto.JourneyOption;
import dto.JourneySearchRequest;
import exception.NoJourneyFoundException;
import exception.ValidationException;
import model.*;
import repository.StationRepository;
import repository.TrainRepository;
import repository.TrainRunRepository;
import repository.TrainRunStopRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SearchService {
    private static final int MIN_TRANSFER_MINUTES = 15;

    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final TrainRunRepository trainRunRepository;
    private final TrainRunStopRepository trainRunStopRepository;

    public SearchService() {
        this.stationRepository = new StationRepository();
        this.trainRepository = new TrainRepository();
        this.trainRunRepository = new TrainRunRepository();
        this.trainRunStopRepository = new TrainRunStopRepository();
    }

    public List<JourneyOption> searchJourneys(JourneySearchRequest request) {
        validateRequest(request);

        String departureCode = request.getDepartureStationCode().trim().toUpperCase();
        String arrivalCode = request.getArrivalStationCode().trim().toUpperCase();
        LocalDate travelDate = request.getTravelDate();

        Station departureStation = stationRepository.findByCode(departureCode)
                .orElseThrow(() -> new ValidationException("Departure station does not exist: " + departureCode));

        Station arrivalStation = stationRepository.findByCode(arrivalCode)
                .orElseThrow(() -> new ValidationException("Arrival station does not exist: " + arrivalCode));

        if (!departureStation.isActive()) {
            throw new ValidationException("Departure station is inactive: " + departureCode);
        }

        if (!arrivalStation.isActive()) {
            throw new ValidationException("Arrival station is inactive: " + arrivalCode);
        }

        List<JourneyOption> journeyOptions = new ArrayList<>();

        journeyOptions.addAll(findDirectJourneys(departureStation, arrivalStation, travelDate));
        journeyOptions.addAll(findOneTransferJourneys(departureStation, arrivalStation, travelDate));

        journeyOptions.sort(Comparator
                .comparing(JourneyOption::getDepartureTime)
                .thenComparing(JourneyOption::getArrivalTime));

        if (journeyOptions.isEmpty()) {
            throw new NoJourneyFoundException(
                    "No journey found from " + departureCode + " to " + arrivalCode + " on " + travelDate
            );
        }

        for (int i = 0; i < journeyOptions.size(); i++) {
            journeyOptions.get(i).setOptionNumber(i + 1);
        }

        return journeyOptions;
    }

    public List<JourneyOption> searchJourneys(String departureCode, String arrivalCode, LocalDate travelDate) {
        JourneySearchRequest request = new JourneySearchRequest(departureCode, arrivalCode, travelDate);
        return searchJourneys(request);
    }

    private List<JourneyOption> findDirectJourneys(Station departureStation, Station arrivalStation, LocalDate travelDate) {
        List<JourneyOption> results = new ArrayList<>();

        List<JourneyLeg> directLegs = findPossibleLegs(
                departureStation.getId(),
                arrivalStation.getId(),
                travelDate,
                1
        );

        for (JourneyLeg leg : directLegs) {
            results.add(new JourneyOption(0, List.of(leg)));
        }

        return results;
    }

    private List<JourneyOption> findOneTransferJourneys(Station departureStation, Station arrivalStation, LocalDate travelDate) {
        List<JourneyOption> results = new ArrayList<>();

        List<Station> stations = stationRepository.findAllActive();

        for (Station transferStation : stations) {
            if (transferStation.getId().equals(departureStation.getId())
                    || transferStation.getId().equals(arrivalStation.getId())) {
                continue;
            }

            List<JourneyLeg> firstLegs = findPossibleLegs(
                    departureStation.getId(),
                    transferStation.getId(),
                    travelDate,
                    1
            );

            List<JourneyLeg> secondLegs = findPossibleLegs(
                    transferStation.getId(),
                    arrivalStation.getId(),
                    travelDate,
                    2
            );

            for (JourneyLeg firstLeg : firstLegs) {
                for (JourneyLeg secondLeg : secondLegs) {
                    if (isValidTransfer(firstLeg, secondLeg)) {
                        results.add(new JourneyOption(0, List.of(firstLeg, secondLeg)));
                    }
                }
            }
        }

        return results;
    }

    private List<JourneyLeg> findPossibleLegs(Long departureStationId, Long arrivalStationId,
                                              LocalDate travelDate, int legOrder) {
        List<JourneyLeg> results = new ArrayList<>();

        List<TrainRun> trainRuns = trainRunRepository.findByServiceDate(travelDate);

        for (TrainRun trainRun : trainRuns) {
            if (!isTrainRunSearchable(trainRun)) {
                continue;
            }

            List<TrainRunStop> stops = trainRunStopRepository.findByTrainRunId(trainRun.getId());

            Optional<TrainRunStop> departureStopOptional = stops.stream()
                    .filter(stop -> stop.getStationId().equals(departureStationId))
                    .findFirst();

            Optional<TrainRunStop> arrivalStopOptional = stops.stream()
                    .filter(stop -> stop.getStationId().equals(arrivalStationId))
                    .findFirst();

            if (departureStopOptional.isEmpty() || arrivalStopOptional.isEmpty()) {
                continue;
            }

            TrainRunStop departureStop = departureStopOptional.get();
            TrainRunStop arrivalStop = arrivalStopOptional.get();

            if (departureStop.getStopOrder() >= arrivalStop.getStopOrder()) {
                continue;
            }

            LocalDateTime departureTime = getDepartureTime(departureStop);
            LocalDateTime arrivalTime = getArrivalTime(arrivalStop);

            if (departureTime == null || arrivalTime == null) {
                continue;
            }

            JourneyLeg leg = buildJourneyLeg(
                    legOrder,
                    trainRun,
                    departureStop,
                    arrivalStop,
                    departureTime,
                    arrivalTime
            );

            results.add(leg);
        }

        results.sort(Comparator.comparing(JourneyLeg::getDepartureTime));
        return results;
    }

    private JourneyLeg buildJourneyLeg(int legOrder, TrainRun trainRun, TrainRunStop departureStop,
                                       TrainRunStop arrivalStop, LocalDateTime departureTime,
                                       LocalDateTime arrivalTime) {
        Train train = trainRepository.findById(trainRun.getTrainId())
                .orElseThrow(() -> new ValidationException("Train not found for train run: " + trainRun.getRunCode()));

        Station departureStation = stationRepository.findById(departureStop.getStationId())
                .orElseThrow(() -> new ValidationException("Departure station not found."));

        Station arrivalStation = stationRepository.findById(arrivalStop.getStationId())
                .orElseThrow(() -> new ValidationException("Arrival station not found."));

        LocalDateTime effectiveDepartureTime = departureTime.plusMinutes(trainRun.getDelayMinutes());
        LocalDateTime effectiveArrivalTime = arrivalTime.plusMinutes(trainRun.getDelayMinutes());

        return new JourneyLeg(
                legOrder,
                trainRun.getId(),
                trainRun.getRunCode(),
                train.getTrainNumber(),
                train.getName(),
                departureStop.getId(),
                arrivalStop.getId(),
                departureStation.getCode(),
                departureStation.getName(),
                arrivalStation.getCode(),
                arrivalStation.getName(),
                effectiveDepartureTime,
                effectiveArrivalTime,
                trainRun.getDelayMinutes()
        );
    }

    private boolean isValidTransfer(JourneyLeg firstLeg, JourneyLeg secondLeg) {
        if (firstLeg.getTrainRunId().equals(secondLeg.getTrainRunId())) {
            return false;
        }

        LocalDateTime minimumSecondDeparture = firstLeg.getArrivalTime().plusMinutes(MIN_TRANSFER_MINUTES);

        return !secondLeg.getDepartureTime().isBefore(minimumSecondDeparture);
    }

    private boolean isTrainRunSearchable(TrainRun trainRun) {
        return trainRun.getStatus() == TrainRunStatus.SCHEDULED
                || trainRun.getStatus() == TrainRunStatus.DELAYED;
    }

    private LocalDateTime getDepartureTime(TrainRunStop stop) {
        if (stop.getPlannedDeparture() != null) {
            return stop.getPlannedDeparture();
        }

        return stop.getPlannedArrival();
    }

    private LocalDateTime getArrivalTime(TrainRunStop stop) {
        if (stop.getPlannedArrival() != null) {
            return stop.getPlannedArrival();
        }

        return stop.getPlannedDeparture();
    }

    private void validateRequest(JourneySearchRequest request) {
        if (request == null) {
            throw new ValidationException("Search request cannot be null.");
        }

        if (request.getDepartureStationCode() == null || request.getDepartureStationCode().isBlank()) {
            throw new ValidationException("Departure station code is required.");
        }

        if (request.getArrivalStationCode() == null || request.getArrivalStationCode().isBlank()) {
            throw new ValidationException("Arrival station code is required.");
        }

        if (request.getTravelDate() == null) {
            throw new ValidationException("Travel date is required.");
        }

        if (request.getDepartureStationCode().trim().equalsIgnoreCase(request.getArrivalStationCode().trim())) {
            throw new ValidationException("Departure and arrival stations must be different.");
        }
    }
}