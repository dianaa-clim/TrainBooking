package com.trainbooking.train_booking_backend.delay;

import com.trainbooking.train_booking_backend.delay.dto.DelayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/delays")
public class AdminDelayHistoryController {

    private final TrainDelayService trainDelayService;

    public AdminDelayHistoryController(TrainDelayService trainDelayService) {
        this.trainDelayService = trainDelayService;
    }

    @GetMapping
    public ResponseEntity<List<DelayResponse>> getAllDelays() {
        return ResponseEntity.ok(trainDelayService.getAllDelays());
    }
}