package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.repository.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupations")
public class OccupationController {

    private final OccupationRepository occupationRepository;

    @GetMapping("/id")
    public ResponseEntity<?> getOccupationId(@RequestParam("jobRoleName") String jobRoleName) {
        List<Occupation> occupations = occupationRepository.findByJobRoleName(jobRoleName);

        if (occupations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "직무명을 찾을 수 없습니다."));
        }

        // 여러 개 중 첫 번째 Occupation 반환 (또는 다른 로직 필요)
        return ResponseEntity.ok(Map.of("id", occupations.get(0).getId()));
    }

    @GetMapping("/name")
    public ResponseEntity<?> getOccupationName(@RequestParam("id") Long id) {
        Occupation occupation = occupationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(Map.of(
                "jobGroup", occupation.getJobGroupName(),
                "jobType", occupation.getJobTypeName(),
                "jobRoleName", occupation.getJobRoleName()
        ));
    }
}
