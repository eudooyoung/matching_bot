//package com.multi.matchingbot.common.service;
//
//import com.multi.matchingbot.common.domain.dto.ResumeDto;
//import com.multi.matchingbot.common.repository.ResumeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ResumeService {
//
//    private final ResumeRepository resumeRepository;
//
//    public Page<ResumeDto> getPageResumes(Pageable pageable) {
//        return resumeRepository.findAll(pageable)
//                .map(ResumeDto::fromEntity);
//    }
//}
