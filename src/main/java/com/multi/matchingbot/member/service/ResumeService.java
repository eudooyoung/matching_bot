package com.multi.matchingbot.member.service;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.dto.ResumeViewLogDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.domain.entity.ResumeViewLog;
import com.multi.matchingbot.member.repository.ResumeRepository;
import com.multi.matchingbot.member.repository.ResumeViewLogRepository;
import com.multi.matchingbot.resume.domain.CareerType;
import com.multi.matchingbot.resume.domain.dto.CareerUpdateDto;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.resume.domain.entity.Career;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeInsertMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeViewLogRepository resumeViewLogRepository;
    private final ResumeBookmarkRepository resumeBookmarkRepository;
    private final OccupationService occupationService;
    private final ResumeInsertMapper resumeInsertMapper;

    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 이력서를 찾을 수 없습니다."));
    }

    public Resume findByIdWithOccupation(Long id) {
        return resumeRepository.findWithOccupationById(id)
                .orElseThrow(() -> new EntityNotFoundException("이력서를 찾을 수 없습니다."));
    }

    public void deleteAllByIds(List<Long> ids) {
        resumeRepository.deleteAllById(ids);
    }

    public List<Resume> findByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId);
    }

    public Page<ResumeDto> getAllResumes(Pageable pageable) {
        return resumeRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Transactional
    public Resume save(Resume resume) {
        return resumeRepository.save(resume);
    }

    private ResumeDto toDto(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .traitKeywords(resume.getTraitKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .memberName(resume.getMember().getName())  // join 필요
                .bookmarked(false) // 모든 이력서 → 관심 여부는 false
                .build();
    }

    public List<ResumeViewLogDto> getResumeViewLogs(Long memberId) {
        List<ResumeViewLog> logs = resumeViewLogRepository.findByResume_Member_IdOrderByViewedAtDesc(memberId);

        return logs.stream().map(log -> ResumeViewLogDto.builder()
                .companyName(log.getCompany().getName())
                .industry(log.getCompany().getIndustry())
                .viewedAt(log.getViewedAt())
                .build()
        ).collect(Collectors.toList());
    }

    public Page<ResumeDto> getPageResumes(Pageable pageable) {
        Page<Resume> resumePage = resumeRepository.findAll(pageable);
        return resumePage.map(ResumeDto::fromEntity); // 여기가 실제 매핑 핵심
    }

    public List<Long> findBookmarkedResumeIdsByCompanyId(Long companyId) {
        return resumeBookmarkRepository.findResumeIdsByCompanyId(companyId);
    }

    @Transactional
    public void insertResume(ResumeInsertDto dto, Member member) {
        Occupation occupation = occupationService.findById(dto.getOccupationId());

        if (dto.getCareerType() == CareerType.NEW)
            dto.setCareers(Collections.emptyList());  // 신입 일때 career 처리

        Resume resume = resumeInsertMapper.toEntity(dto);
        resume.setMember(member);
        resume.setOccupation(occupation);

        if (resume.getCareers() != null)
            resume.getCareers().forEach(c -> c.setResume(resume));

        resumeRepository.save(resume);
    }

    @Transactional
    public void updateResume(ResumeUpdateDto dto, Member member) {
        Resume resume = resumeRepository.findByIdAndMember(dto.getId(), member)
                .orElseThrow(() -> new EntityNotFoundException("이력서를 찾을 수 없습니다."));

        // 기본 필드 업데이트
        resume.updateBasicFields(dto);

        // 관심 직무
        Occupation occupation = occupationService.findById(dto.getOccupationId());
        resume.setOccupation(occupation);

        // 경력 처리 (신입이면 비우기)
        if (dto.getCareerType() == CareerType.NEW) {
            resume.getCareers().clear();
        } else {
            resume.getCareers().clear();
            for (CareerUpdateDto c : dto.getCareers()) {
                Career career = Career.builder()
                        .companyName(c.getCompanyName())
                        .departmentName(c.getDepartmentName())
                        .positionTitle(c.getPositionTitle())
                        .salary(c.getSalary())
                        .careerSummary(c.getCareerSummary())
                        .startDate(c.getStartDate())
                        .endDate(c.getEndDate())
                        .resume(resume)
                        .build();
                resume.getCareers().add(career);
            }
        }

        resumeRepository.save(resume);
    }

    public Resume findByIdAndMemberWithOccupation(Long id, Member member) {
        return resumeRepository.findWithOccupationByIdAndMember(id, member)
                .orElseThrow(() -> new EntityNotFoundException("해당 이력서를 찾을 수 없습니다."));
    }

    public Page<ResumeDto> searchResumes(String jobGroup, String jobType, String jobRole, String careerType, String companyName, Pageable pageable) {
        return resumeRepository.searchWithFilters(jobGroup, jobType, jobRole, careerType, companyName, pageable)
                .map(ResumeDto::fromEntity);
    }

//    public List<ResumeDto> searchResumes(String jobGroup, String jobType, String jobRole, String careerType, String companyName) {
//        // 원하는 필터 조건으로 repository에 쿼리 작성
//        return resumeRepository.findByFilters(jobGroup, jobType, jobRole, careerType, companyName)
//                .stream()
//                .map(ResumeDto::fromEntity)
//                .collect(Collectors.toList());
//        }

}
