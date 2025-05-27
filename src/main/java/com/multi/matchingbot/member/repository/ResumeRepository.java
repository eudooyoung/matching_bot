package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entities.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findAll(Pageable pageable);
}
