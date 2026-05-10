package com.trainbooking.train_booking_backend.delay;

import com.trainbooking.train_booking_backend.delay.dto.DelayRequest;
import com.trainbooking.train_booking_backend.delay.dto.DelayResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/train-runs/{trainRunId}/delays")
public class AdminDelayController {

    private final TrainDelayService trainDelayService;

    public AdminDelayController(TrainDelayService trainDelayService) {
        this.trainDelayService = trainDelayService;
    }

    @PostMapping
    public ResponseEntity<DelayResponse> registerDelay(
            @PathVariable Long trainRunId,
            @Valid @RequestBody DelayRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                trainDelayService.registerDelay(
                        trainRunId,
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<DelayResponse>> getDelaysForTrainRun(
            @PathVariable Long trainRunId
    ) {
        return ResponseEntity.ok(
                trainDelayService.getDelaysForTrainRun(trainRunId)
        );
    }
}