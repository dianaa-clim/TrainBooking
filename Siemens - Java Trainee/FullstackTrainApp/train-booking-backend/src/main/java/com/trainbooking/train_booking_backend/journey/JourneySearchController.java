package com.trainbooking.train_booking_backend.journey;

import com.trainbooking.train_booking_backend.journey.dto.JourneyOptionResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer/journeys")
public class JourneySearchController {

    private final JourneySearchService journeySearchService;

    public JourneySearchController(JourneySearchService journeySearchService) {
        this.journeySearchService = journeySearchService;
    }

    @GetMapping
    public ResponseEntity<List<JourneyOptionResponse>> searchJourneys(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
                journeySearchService.searchJourneys(from, to, date)
        );
    }
}