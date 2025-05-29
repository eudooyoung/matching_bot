package com.multi.matchingbot.job.service;

import com.multi.matchingbot.job.domain.dto.JobGroupDto;
import com.multi.matchingbot.job.domain.dto.JobRoleDto;
import com.multi.matchingbot.job.domain.dto.JobTypeDto;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.repository.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final OccupationRepository occupationRepository;

    /**
     * 직군-직종-직무 계층 구조 반환
     */
    public List<JobGroupDto> getOccupationHierarchy() {
        List<Occupation> occupations = occupationRepository.findAll();

        // 그룹화: jobGroupCode → jobTypeCode → 직무 리스트
        Map<Long, Map<Long, List<Occupation>>> grouped = occupations.stream()
                .collect(Collectors.groupingBy(
                        Occupation::getJobGroupCode,
                        Collectors.groupingBy(Occupation::getJobTypeCode)
                ));

        List<JobGroupDto> jobGroupDtos = new ArrayList<>();

        for (Map.Entry<Long, Map<Long, List<Occupation>>> groupEntry : grouped.entrySet()) {
            Long jobGroupCode = groupEntry.getKey();
            List<Occupation> groupSample = groupEntry.getValue().values().stream().flatMap(List::stream).toList();
            String jobGroupName = groupSample.get(0).getJobGroupName();

            List<JobTypeDto> jobTypeDtos = new ArrayList<>();
            for (Map.Entry<Long, List<Occupation>> typeEntry : groupEntry.getValue().entrySet()) {
                Long jobTypeCode = typeEntry.getKey();
                String jobTypeName = typeEntry.getValue().get(0).getJobTypeName();

                List<JobRoleDto> jobRoleDtos = typeEntry.getValue().stream()
                        .map(o -> new JobRoleDto(o.getId(), o.getJobRoleCode(), o.getJobRoleName()))
                        .collect(Collectors.toList());

                jobTypeDtos.add(new JobTypeDto(jobTypeCode, jobTypeName, jobRoleDtos));
            }

            jobGroupDtos.add(new JobGroupDto(jobGroupCode, jobGroupName, jobTypeDtos));
        }

        return jobGroupDtos;
    }

    /**
     * ID로 Occupation 엔티티 조회
     */
    public Occupation findById(Long id) {
        return occupationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 직무가 존재하지 않습니다. id: " + id));
    }
}
