package com.multi.matchingbot.job.mapper;

import com.multi.matchingbot.job.domain.dto.ResumeBookmarkDto;
import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResumeBookmarkMapper {

    ResumeBookmarkDto toDto(ResumeBookmark entity);

    ResumeBookmark toEntity(ResumeBookmarkDto dto);

    ResumeDto toResumeDto(Resume resume);
}
