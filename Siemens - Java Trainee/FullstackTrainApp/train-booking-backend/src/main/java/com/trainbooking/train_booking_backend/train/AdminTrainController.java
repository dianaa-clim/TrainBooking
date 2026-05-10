package com.trainbooking.train_booking_backend.train;

import com.trainbooking.train_booking_backend.train.dto.TrainRequest;
import com.trainbooking.train_booking_backend.train.dto.TrainResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trains")
public class AdminTrainController {

    private final TrainService trainService;

    public AdminTrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<TrainResponse>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainResponse> getTrainById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @PostMapping
    public ResponseEntity<TrainResponse> createTrain(
            @Valid @RequestBody TrainRequest request
    ) {
        return ResponseEntity.ok(trainService.createTrain(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainResponse> updateTrain(
            @PathVariable Long id,
            @Valid @RequestBody TrainRequest request
    ) {
        return ResponseEntity.ok(trainService.updateTrain(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TrainResponse> deactivateTrain(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainService.deactivateTrain(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TrainResponse> activateTrain(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainService.activateTrain(id));
    }
}