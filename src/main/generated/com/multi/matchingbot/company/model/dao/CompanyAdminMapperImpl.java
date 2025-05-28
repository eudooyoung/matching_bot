package com.multi.matchingbot.company.model.dao;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyAdminView;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T09:50:36+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class CompanyAdminMapperImpl implements CompanyAdminMapper {

    @Override
    public CompanyAdminView toCompanyAdminView(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyAdminView companyAdminView = new CompanyAdminView();

        companyAdminView.setId( company.getId() );
        companyAdminView.setName( CompanyAdminMapper.maskBusinessNo( company.getName() ) );
        companyAdminView.setEmail( CompanyAdminMapper.maskBusinessNo( company.getEmail() ) );
        companyAdminView.setPhone( CompanyAdminMapper.maskBusinessNo( company.getPhone() ) );
        companyAdminView.setAddress( CompanyAdminMapper.maskBusinessNo( company.getAddress() ) );
        companyAdminView.setIndustry( CompanyAdminMapper.maskBusinessNo( company.getIndustry() ) );
        companyAdminView.setReportStatus( company.getReportStatus() );
        companyAdminView.setStatus( company.getStatus() );
        companyAdminView.setCreatedAt( company.getCreatedAt() );
        companyAdminView.setUpdatedBy( CompanyAdminMapper.maskBusinessNo( company.getUpdatedBy() ) );
        companyAdminView.setUpdatedAt( company.getUpdatedAt() );

        companyAdminView.setFormattedId( CompanyAdminMapper.formatId(company.getId()) );
        companyAdminView.setMaskedNo( CompanyAdminMapper.maskBusinessNo(company.getBusinessNo()) );
        companyAdminView.setAgreementValid( CompanyAdminMapper.isAgreementValid(company) );

        return companyAdminView;
    }
}
