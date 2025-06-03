package com.multi.matchingbot.job.mapper;

import com.multi.matchingbot.job.domain.dto.ResumeBookmarkDto;
import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResumeBookmarkMapper {
    ResumeBookmarkDto toDto(ResumeBookmark entity);
    ResumeBookmark toEntity(ResumeBookmarkDto dto);
}