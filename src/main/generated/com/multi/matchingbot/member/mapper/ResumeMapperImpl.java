package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.job.Occupation;
import com.multi.matchingbot.member.domain.dtos.ResumeAdminViewDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T10:24:34+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ResumeMapperImpl implements ResumeMapper {

    @Override
    public ResumeAdminViewDto toResumeAdminView(Resume resume) {
        if ( resume == null ) {
            return null;
        }

        ResumeAdminViewDto resumeAdminViewDto = new ResumeAdminViewDto();

        resumeAdminViewDto.setId( resume.getId() );
        resumeAdminViewDto.setTitle( resume.getTitle() );
        resumeAdminViewDto.setSkillKeywords( resume.getSkillKeywords() );
        resumeAdminViewDto.setTalentKeywords( resume.getTalentKeywords() );
        resumeAdminViewDto.setDesiredOccupation( resumeOccupationJobRoleName( resume ) );
        resumeAdminViewDto.setCreatedBy( resume.getCreatedBy() );
        resumeAdminViewDto.setCreatedAt( resume.getCreatedAt() );
        resumeAdminViewDto.setUpdatedBy( resume.getUpdatedBy() );
        resumeAdminViewDto.setUpdatedAt( resume.getUpdatedAt() );

        resumeAdminViewDto.setFormattedId( formatId(resume.getId()) );

        return resumeAdminViewDto;
    }

    private String resumeOccupationJobRoleName(Resume resume) {
        Occupation occupation = resume.getOccupation();
        if ( occupation == null ) {
            return null;
        }
        return occupation.getJobRoleName();
    }
}
