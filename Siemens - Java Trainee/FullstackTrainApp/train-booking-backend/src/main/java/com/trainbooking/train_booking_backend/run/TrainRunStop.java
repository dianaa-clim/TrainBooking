package com.trainbooking.train_booking_backend.run;

import com.trainbooking.train_booking_backend.common.BaseEntity;
import com.trainbooking.train_booking_backend.station.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "train_run_stops",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_train_run_stop_order", columnNames = {"train_run_id", "stop_order"})
        }
)
public class TrainRunStop extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_run_id", nullable = false)
    private TrainRun trainRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_order", nullable = false)
    private int stopOrder;

    @Column(name = "planned_arrival_time")
    private LocalDateTime plannedArrivalTime;

    @Column(name = "planned_departure_time")
    private LocalDateTime plannedDepartureTime;

    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    @Column(name = "actual_departure_time")
    private LocalDateTime actualDepartureTime;
}