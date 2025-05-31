package com.multi.matchingbot.job.service;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.repository.OccupationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final OccupationRepository occupationRepository;

    public Occupation findById(Long id) {
        return occupationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 직무가 존재하지 않습니다. id: " + id));
    }

//    public Map<String, Long> getOccupationNameIdMap() {
//        return occupationRepository.findAll().stream()
//                .collect(Collectors.toMap(Occupation::getJobRoleName, Occupation::getId));
//    }

    public Long findIdByJobRoleName(String jobRoleName) {
        return occupationRepository.findByJobRoleName(jobRoleName)
                .map(Occupation::getId)
                .orElseThrow(() -> new EntityNotFoundException("해당 직무명이 존재하지 않습니다: " + jobRoleName));
    }
}
