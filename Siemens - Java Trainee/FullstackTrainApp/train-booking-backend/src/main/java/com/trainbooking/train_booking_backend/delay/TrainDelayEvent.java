package com.trainbooking.train_booking_backend.delay;

import com.trainbooking.train_booking_backend.common.BaseEntity;
import com.trainbooking.train_booking_backend.run.TrainRun;
import com.trainbooking.train_booking_backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "train_delay_events")
public class TrainDelayEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_run_id", nullable = false)
    private TrainRun trainRun;

    @Column(name = "delay_minutes", nullable = false)
    private int delayMinutes;

    @Column(nullable = false, length = 500)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
}