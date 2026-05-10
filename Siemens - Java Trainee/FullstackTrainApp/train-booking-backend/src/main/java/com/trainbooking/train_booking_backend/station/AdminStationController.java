package com.trainbooking.train_booking_backend.station;

import com.trainbooking.train_booking_backend.station.dto.StationRequest;
import com.trainbooking.train_booking_backend.station.dto.StationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stations")
public class AdminStationController {

    private final StationService stationService;

    public AdminStationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> getStationById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
            @Valid @RequestBody StationRequest request
    ) {
        return ResponseEntity.ok(stationService.createStation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponse> updateStation(
            @PathVariable Long id,
            @Valid @RequestBody StationRequest request
    ) {
        return ResponseEntity.ok(stationService.updateStation(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StationResponse> deactivateStation(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(stationService.deactivateStation(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StationResponse> activateStation(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(stationService.activateStation(id));
    }
}