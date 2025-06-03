package com.multi.matchingbot.mapposting.service;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.mapposting.domain.MapPostingDto;
import com.multi.matchingbot.mapposting.repository.MapPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapPostingService {

    private final MapPostingRepository mapPostingRepository;

    public List<MapPostingDto> getNearbyPostings(double lat, double lng, double radiusKm) {
        List<Job> allJobs = mapPostingRepository.findAll();
        List<MapPostingDto> result = new ArrayList<>();

        for (Job job : allJobs) {
            Double jobLat = job.getLatitude();
            Double jobLng = job.getLongitude();

            if (jobLat == null || jobLng == null) continue;

            double distance = calculateDistance(lat, lng, jobLat, jobLng);
            if (distance <= radiusKm) {
                result.add(MapPostingDto.fromEntity(job));
            }
        }
        return result;
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
