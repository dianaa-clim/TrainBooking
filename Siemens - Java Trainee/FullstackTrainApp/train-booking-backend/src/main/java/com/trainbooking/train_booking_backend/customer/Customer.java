package com.trainbooking.train_booking_backend.customer;

import com.trainbooking.train_booking_backend.booking.Booking;
import com.trainbooking.train_booking_backend.common.BaseEntity;
import com.trainbooking.train_booking_backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();
}