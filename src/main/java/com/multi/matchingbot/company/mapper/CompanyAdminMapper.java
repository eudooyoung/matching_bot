package com.multi.matchingbot.company.mapper;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyAdminView;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Primary
@Mapper(componentModel = "spring")
public interface CompanyAdminMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formattedId", expression = "java(CompanyAdminMapper.formatId(company.getId()))")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "businessNo", source = "businessNo")
//    @Mapping(target = "maskedNo", expression = "java(maskBusinessNo(company.getBusinessNo()))")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "industry", source = "industry")
    @Mapping(target = "agreementValid", expression = "java(CompanyAdminMapper.isAgreementValid(company))")
    @Mapping(target = "reportStatus", source = "reportStatus")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CompanyAdminView toCompanyAdminView(Company company);

    static String formatId(Long id) {
        return String.format("C%05d", id);
    }

//    default String maskBusinessNo(String businessNo) {
//        if (businessNo == null || businessNo.length() < 10) return "";
//        return businessNo.substring(0, 2) + "-**-" + businessNo.substring(6);
//    }

    static boolean isAgreementValid(Company company) {
        return company.getAgreeTerms() == Yn.Y
                && company.getAgreePrivacy() == Yn.Y
                && company.getAgreeFinance() == Yn.Y;
    }

}
