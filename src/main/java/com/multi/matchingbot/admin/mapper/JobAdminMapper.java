package com.multi.matchingbot.admin.mapper;

import com.multi.matchingbot.admin.domain.JobAdminView;
import com.multi.matchingbot.job.domain.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JobAdminMapper {

    @Mapping(target = "formattedId", expression = "java(JobAdminMapper.formatId(job.getId()))")
    @Mapping(source = "company.name", target = "companyName", qualifiedByName = "nullableString")
    @Mapping(source = "occupation.jobRoleName", target = "occupationName", qualifiedByName = "nullableString")
    JobAdminView toJobAdminView(Job job);

    @Named("nullableString")
    static String nullableString(String value) {
        return value != null ? value : "(미지정)";
    }

    static String formatId(Long id) {
            return String.format("J%05d", id);
        }
}
