package com.trainbooking.train_booking_backend.delay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainDelayEventRepository extends JpaRepository<TrainDelayEvent, Long> {

    List<TrainDelayEvent> findByTrainRunIdOrderByCreatedAtDesc(Long trainRunId);

    List<TrainDelayEvent> findAllByOrderByCreatedAtDesc();
}