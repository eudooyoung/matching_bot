package com.multi.matchingbot.member.service;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.repository.ResumeRepository;
import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.career.domain.CareerUpdateDto;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeInsertMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberResumeService {

    private final OccupationService occupationService;
    private final ResumeInsertMapper resumeInsertMapper;
    private final ResumeRepository resumeRepository;

    /**
     * 이력서 등록 서비스 메소드
     *
     * @param dto    이력서 등록용 dto
     * @param member 이력서 테이블에 member 수동 주입
     */
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

    /**
     * 이력서 수정 서비스 메소드
     *
     * @param dto    이력서 수정용 dto
     * @param member 로그인한 member entity
     */
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

    public Optional<Resume> findByIdWithAll(Long resumeId) {
        return resumeRepository.findByIdWithAll(resumeId);
    }
}
