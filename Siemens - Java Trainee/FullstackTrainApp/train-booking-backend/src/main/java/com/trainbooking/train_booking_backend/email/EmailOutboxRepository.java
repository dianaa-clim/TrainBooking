package com.trainbooking.train_booking_backend.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailOutboxRepository extends JpaRepository<EmailOutbox, Long> {

    List<EmailOutbox> findAllByOrderByCreatedAtDesc();

    List<EmailOutbox> findByStatusOrderByCreatedAtDesc(EmailStatus status);

    List<EmailOutbox> findByEmailTypeOrderByCreatedAtDesc(EmailType emailType);
}