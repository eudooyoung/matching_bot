package com.multi.matchingbot.member.service;

import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.repository.JobRepository;
import com.multi.matchingbot.member.domain.entity.JobBookmark;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.JobBookmarkRepository;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobBookmarkService {

    private final JobBookmarkRepository jobBookmarkRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;

    @Transactional
    public void addJobBookmark(Long memberId, Long jobId) {
        if (!jobBookmarkRepository.existsByMemberIdAndJobId(memberId, jobId)) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
            Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("채용공고를 찾을 수 없습니다."));

            JobBookmark jobBookmark = JobBookmark.builder()
                    .member(member)
                    .job(job)
                    .build();
            jobBookmarkRepository.save(jobBookmark);
        }
    }

    @Transactional
    public void removeJobBookmark(Long memberId, Long jobId) {
        jobBookmarkRepository.deleteByMemberIdAndJobId(memberId, jobId);
    }

    public boolean isBookmarked(Long memberId, Long jobId) {
        return jobBookmarkRepository.existsByMemberIdAndJobId(memberId, jobId);
    }

    public List<Long> getBookmarkedJobIds(Long memberId) {
        return jobBookmarkRepository.findJobIdsByMemberId(memberId);
    }

    @Transactional
    public void removeJobBookmarks(Long memberId, List<Long> jobIds) {
        for (Long jobId : jobIds) {
            jobBookmarkRepository.deleteByMemberIdAndJobId(memberId, jobId);
        }
    }

    public Page<JobDto> getBookmarkedJobs(Long memberId, Pageable pageable) {
        return jobBookmarkRepository.findJobDtosByMemberId(memberId, pageable);
    }

    public List<Long> getBookmarkedJobIdsForMember(Long memberId) {
        return jobBookmarkRepository.findJobIdsByMemberId(memberId);
    }
}