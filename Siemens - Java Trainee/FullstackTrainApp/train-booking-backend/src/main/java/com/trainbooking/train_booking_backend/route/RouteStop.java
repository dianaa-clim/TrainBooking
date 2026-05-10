package com.trainbooking.train_booking_backend.route;

import com.trainbooking.train_booking_backend.common.BaseEntity;
import com.trainbooking.train_booking_backend.station.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "route_stops",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_route_stop_order", columnNames = {"route_id", "stop_order"})
        }
)
public class RouteStop extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_order", nullable = false)
    private int stopOrder;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(nullable = false)
    private boolean active = true;
}