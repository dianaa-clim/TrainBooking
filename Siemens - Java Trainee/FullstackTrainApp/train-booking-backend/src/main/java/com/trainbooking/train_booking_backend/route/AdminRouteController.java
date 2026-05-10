package com.trainbooking.train_booking_backend.route;

import com.trainbooking.train_booking_backend.route.dto.RouteRequest;
import com.trainbooking.train_booking_backend.route.dto.RouteResponse;
import com.trainbooking.train_booking_backend.route.dto.RouteStopRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/routes")
public class AdminRouteController {

    private final RouteService routeService;

    public AdminRouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request
    ) {
        return ResponseEntity.ok(routeService.createRoute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteRequest request
    ) {
        return ResponseEntity.ok(routeService.updateRoute(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<RouteResponse> deactivateRoute(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(routeService.deactivateRoute(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RouteResponse> activateRoute(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(routeService.activateRoute(id));
    }

    @PostMapping("/{routeId}/stops")
    public ResponseEntity<RouteResponse> addStopToRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody RouteStopRequest request
    ) {
        return ResponseEntity.ok(routeService.addStopToRoute(routeId, request));
    }

    @PutMapping("/{routeId}/stops/{stopId}")
    public ResponseEntity<RouteResponse> updateRouteStop(
            @PathVariable Long routeId,
            @PathVariable Long stopId,
            @Valid @RequestBody RouteStopRequest request
    ) {
        return ResponseEntity.ok(routeService.updateRouteStop(routeId, stopId, request));
    }

    @DeleteMapping("/{routeId}/stops/{stopId}")
    public ResponseEntity<RouteResponse> removeStopFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long stopId
    ) {
        return ResponseEntity.ok(routeService.removeStopFromRoute(routeId, stopId));
    }
}