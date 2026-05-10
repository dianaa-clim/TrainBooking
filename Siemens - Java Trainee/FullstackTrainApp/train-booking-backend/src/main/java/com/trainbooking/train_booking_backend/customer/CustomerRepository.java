package com.trainbooking.train_booking_backend.customer;

import com.trainbooking.train_booking_backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUser(User user);

    Optional<Customer> findByUserId(Long userId);

    Optional<Customer> findByUserEmail(String email);
}