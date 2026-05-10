package com.trainbooking.train_booking_backend.run;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainRunStopRepository extends JpaRepository<TrainRunStop, Long> {

    List<TrainRunStop> findByTrainRunIdOrderByStopOrderAsc(Long trainRunId);

    Optional<TrainRunStop> findByTrainRunIdAndStopOrder(Long trainRunId, int stopOrder);

    Optional<TrainRunStop> findByTrainRunIdAndStationCode(Long trainRunId, String stationCode);
}