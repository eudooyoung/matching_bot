package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.jobposting.Occupation;
import com.multi.matchingbot.member.domain.dtos.ResumeAdminView;
import com.multi.matchingbot.member.domain.entities.Resume;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T09:50:36+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ResumeMapperImpl implements ResumeMapper {

    @Override
    public ResumeAdminView toResumeAdminView(Resume resume) {
        if ( resume == null ) {
            return null;
        }

        ResumeAdminView resumeAdminView = new ResumeAdminView();

        resumeAdminView.setId( resume.getId() );
        resumeAdminView.setTitle( resume.getTitle() );
        resumeAdminView.setSkillKeywords( resume.getSkillKeywords() );
        resumeAdminView.setTalentKeywords( resume.getTalentKeywords() );
        resumeAdminView.setKeywordsStatus( resume.getKeywordsStatus() );
        resumeAdminView.setDesiredOccupation( resumeOccupationJobRoleName( resume ) );
        resumeAdminView.setCreatedBy( resume.getCreatedBy() );
        resumeAdminView.setCreatedAt( resume.getCreatedAt() );
        resumeAdminView.setUpdatedBy( resume.getUpdatedBy() );
        resumeAdminView.setUpdatedAt( resume.getUpdatedAt() );

        resumeAdminView.setFormattedId( ResumeMapper.formatId(resume.getId()) );

        return resumeAdminView;
    }

    private String resumeOccupationJobRoleName(Resume resume) {
        Occupation occupation = resume.getOccupation();
        if ( occupation == null ) {
            return null;
        }
        return occupation.getJobRoleName();
    }
}
