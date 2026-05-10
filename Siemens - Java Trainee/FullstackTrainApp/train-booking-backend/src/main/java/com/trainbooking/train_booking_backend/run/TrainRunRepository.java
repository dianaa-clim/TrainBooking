package com.trainbooking.train_booking_backend.run;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainRunRepository extends JpaRepository<TrainRun, Long> {

    List<TrainRun> findByRunDateAndActiveTrue(LocalDate runDate);

    List<TrainRun> findByRouteIdAndRunDateAndActiveTrue(Long routeId, LocalDate runDate);

    List<TrainRun> findByTrainIdAndRunDateAndActiveTrue(Long trainId, LocalDate runDate);
}