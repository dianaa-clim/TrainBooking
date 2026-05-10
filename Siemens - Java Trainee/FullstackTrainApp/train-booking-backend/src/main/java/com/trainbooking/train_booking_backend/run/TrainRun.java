package com.trainbooking.train_booking_backend.run;

import com.trainbooking.train_booking_backend.common.BaseEntity;
import com.trainbooking.train_booking_backend.route.Route;
import com.trainbooking.train_booking_backend.train.Train;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "train_runs")
public class TrainRun extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "run_date", nullable = false)
    private LocalDate runDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TrainRunStatus status = TrainRunStatus.SCHEDULED;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "trainRun", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stopOrder ASC")
    private List<TrainRunStop> stops = new ArrayList<>();
}