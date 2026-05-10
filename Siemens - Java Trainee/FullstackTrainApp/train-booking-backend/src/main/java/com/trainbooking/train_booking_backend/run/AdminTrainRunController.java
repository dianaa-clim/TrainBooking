package com.trainbooking.train_booking_backend.run;

import com.trainbooking.train_booking_backend.run.dto.TrainRunRequest;
import com.trainbooking.train_booking_backend.run.dto.TrainRunResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/train-runs")
public class AdminTrainRunController {

    private final TrainRunService trainRunService;

    public AdminTrainRunController(TrainRunService trainRunService) {
        this.trainRunService = trainRunService;
    }

    @GetMapping
    public ResponseEntity<List<TrainRunResponse>> getAllTrainRuns() {
        return ResponseEntity.ok(trainRunService.getAllTrainRuns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainRunResponse> getTrainRunById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainRunService.getTrainRunById(id));
    }

    @PostMapping
    public ResponseEntity<TrainRunResponse> createTrainRun(
            @Valid @RequestBody TrainRunRequest request
    ) {
        return ResponseEntity.ok(trainRunService.createTrainRun(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainRunResponse> updateTrainRun(
            @PathVariable Long id,
            @Valid @RequestBody TrainRunRequest request
    ) {
        return ResponseEntity.ok(trainRunService.updateTrainRun(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TrainRunResponse> deactivateTrainRun(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainRunService.deactivateTrainRun(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TrainRunResponse> activateTrainRun(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trainRunService.activateTrainRun(id));
    }
}