package com.trainbooking.train_booking_backend.train;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByCode(String code);

    boolean existsByCode(String code);

    List<Train> findByActiveTrue();
}