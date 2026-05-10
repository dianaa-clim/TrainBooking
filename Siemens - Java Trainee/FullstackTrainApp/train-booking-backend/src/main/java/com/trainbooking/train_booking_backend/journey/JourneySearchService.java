package com.trainbooking.train_booking_backend.journey;

import com.trainbooking.train_booking_backend.exception.NoJourneyFoundException;
import com.trainbooking.train_booking_backend.journey.dto.JourneyLegResponse;
import com.trainbooking.train_booking_backend.journey.dto.JourneyOptionResponse;
import com.trainbooking.train_booking_backend.run.TrainRun;
import com.trainbooking.train_booking_backend.run.TrainRunRepository;
import com.trainbooking.train_booking_backend.run.TrainRunStop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class JourneySearchService {

    private final TrainRunRepository trainRunRepository;

    public JourneySearchService(TrainRunRepository trainRunRepository) {
        this.trainRunRepository = trainRunRepository;
    }

    @Transactional(readOnly = true)
    public List<JourneyOptionResponse> searchJourneys(
            String fromStationCode,
            String toStationCode,
            LocalDate date
    ) {
        String from = normalizeCode(fromStationCode);
        String to = normalizeCode(toStationCode);

        List<TrainRun> trainRuns = trainRunRepository.findByRunDateAndActiveTrue(date);

        List<JourneyOptionResponse> results = new ArrayList<>();

        results.addAll(findDirectJourneys(trainRuns, from, to));
        results.addAll(findOneChangeJourneys(trainRuns, from, to));

        results.sort(Comparator.comparing(JourneyOptionResponse::getDepartureTime));

        if (results.isEmpty()) {
            throw new NoJourneyFoundException(
                    "No journey found from " + from + " to " + to + " on " + date + "."
            );
        }

        return results;
    }

    private List<JourneyOptionResponse> findDirectJourneys(
            List<TrainRun> trainRuns,
            String from,
            String to
    ) {
        List<JourneyOptionResponse> directJourneys = new ArrayList<>();

        for (TrainRun trainRun : trainRuns) {
            Optional<TrainRunStop> originStopOptional = findStopByStationCode(trainRun, from);
            Optional<TrainRunStop> destinationStopOptional = findStopByStationCode(trainRun, to);

            if (originStopOptional.isEmpty() || destinationStopOptional.isEmpty()) {
                continue;
            }

            TrainRunStop originStop = originStopOptional.get();
            TrainRunStop destinationStop = destinationStopOptional.get();

            if (originStop.getStopOrder() >= destinationStop.getStopOrder()) {
                continue;
            }

            JourneyOptionResponse option = new JourneyOptionResponse();
            option.setType("DIRECT");
            option.setDepartureTime(getDepartureTime(originStop));
            option.setArrivalTime(getArrivalTime(destinationStop));

            JourneyLegResponse leg = buildLegResponse(
                    1,
                    trainRun,
                    originStop,
                    destinationStop
            );

            option.setLegs(List.of(leg));

            directJourneys.add(option);
        }

        return directJourneys;
    }

    private List<JourneyOptionResponse> findOneChangeJourneys(
            List<TrainRun> trainRuns,
            String from,
            String to
    ) {
        List<JourneyOptionResponse> oneChangeJourneys = new ArrayList<>();

        for (TrainRun firstRun : trainRuns) {
            Optional<TrainRunStop> firstOriginOptional = findStopByStationCode(firstRun, from);

            if (firstOriginOptional.isEmpty()) {
                continue;
            }

            TrainRunStop firstOrigin = firstOriginOptional.get();

            for (TrainRunStop transferStopFirstRun : firstRun.getStops()) {
                if (transferStopFirstRun.getStopOrder() <= firstOrigin.getStopOrder()) {
                    continue;
                }

                String transferStationCode = transferStopFirstRun.getStation().getCode();

                if (transferStationCode.equalsIgnoreCase(to)) {
                    continue;
                }

                for (TrainRun secondRun : trainRuns) {
                    if (firstRun.getId().equals(secondRun.getId())) {
                        continue;
                    }

                    Optional<TrainRunStop> transferStopSecondRunOptional =
                            findStopByStationCode(secondRun, transferStationCode);

                    Optional<TrainRunStop> finalDestinationOptional =
                            findStopByStationCode(secondRun, to);

                    if (transferStopSecondRunOptional.isEmpty() || finalDestinationOptional.isEmpty()) {
                        continue;
                    }

                    TrainRunStop transferStopSecondRun = transferStopSecondRunOptional.get();
                    TrainRunStop finalDestination = finalDestinationOptional.get();

                    if (transferStopSecondRun.getStopOrder() >= finalDestination.getStopOrder()) {
                        continue;
                    }

                    LocalDateTime firstArrivalAtTransfer = getArrivalTime(transferStopFirstRun);
                    LocalDateTime secondDepartureFromTransfer = getDepartureTime(transferStopSecondRun);

                    if (firstArrivalAtTransfer == null || secondDepartureFromTransfer == null) {
                        continue;
                    }

                    if (secondDepartureFromTransfer.isBefore(firstArrivalAtTransfer)) {
                        continue;
                    }

                    JourneyOptionResponse option = new JourneyOptionResponse();
                    option.setType("ONE_CHANGE");
                    option.setDepartureTime(getDepartureTime(firstOrigin));
                    option.setArrivalTime(getArrivalTime(finalDestination));
                    option.setTransferStationCode(transferStopFirstRun.getStation().getCode());
                    option.setTransferStationName(transferStopFirstRun.getStation().getName());

                    JourneyLegResponse firstLeg = buildLegResponse(
                            1,
                            firstRun,
                            firstOrigin,
                            transferStopFirstRun
                    );

                    JourneyLegResponse secondLeg = buildLegResponse(
                            2,
                            secondRun,
                            transferStopSecondRun,
                            finalDestination
                    );

                    option.setLegs(List.of(firstLeg, secondLeg));

                    oneChangeJourneys.add(option);
                }
            }
        }

        return oneChangeJourneys;
    }

    private Optional<TrainRunStop> findStopByStationCode(
            TrainRun trainRun,
            String stationCode
    ) {
        return trainRun.getStops()
                .stream()
                .filter(stop -> stop.getStation().getCode().equalsIgnoreCase(stationCode))
                .findFirst();
    }

    private JourneyLegResponse buildLegResponse(
            int legOrder,
            TrainRun trainRun,
            TrainRunStop originStop,
            TrainRunStop destinationStop
    ) {
        JourneyLegResponse response = new JourneyLegResponse();

        response.setLegOrder(legOrder);

        response.setTrainRunId(trainRun.getId());
        response.setTrainCode(trainRun.getTrain().getCode());
        response.setTrainName(trainRun.getTrain().getName());

        response.setRouteId(trainRun.getRoute().getId());
        response.setRouteCode(trainRun.getRoute().getCode());
        response.setRouteName(trainRun.getRoute().getName());

        response.setOriginStopId(originStop.getId());
        response.setOriginStationCode(originStop.getStation().getCode());
        response.setOriginStationName(originStop.getStation().getName());

        response.setDestinationStopId(destinationStop.getId());
        response.setDestinationStationCode(destinationStop.getStation().getCode());
        response.setDestinationStationName(destinationStop.getStation().getName());

        response.setDepartureTime(getDepartureTime(originStop));
        response.setArrivalTime(getArrivalTime(destinationStop));

        return response;
    }

    private LocalDateTime getDepartureTime(TrainRunStop stop) {
        if (stop.getActualDepartureTime() != null) {
            return stop.getActualDepartureTime();
        }

        return stop.getPlannedDepartureTime();
    }

    private LocalDateTime getArrivalTime(TrainRunStop stop) {
        if (stop.getActualArrivalTime() != null) {
            return stop.getActualArrivalTime();
        }

        return stop.getPlannedArrivalTime();
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}