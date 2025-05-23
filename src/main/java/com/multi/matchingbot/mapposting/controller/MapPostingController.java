package com.multi.matchingbot.mapposting.controller;

import com.multi.matchingbot.mapposting.domain.MapPostingDto;
import com.multi.matchingbot.mapposting.service.MapPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class MapPostingController {

    private final MapPostingService mapPostingService;

    @GetMapping("/nearby")
    public ResponseEntity<List<MapPostingDto>> getNearbyJobs(
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lng") double lng,
            @RequestParam(name = "radiusKm", defaultValue = "5.0") double radiusKm) {

        List<MapPostingDto> jobs = mapPostingService.getNearbyPostings(lat, lng, radiusKm);
        return ResponseEntity.ok(jobs);
    }

}

