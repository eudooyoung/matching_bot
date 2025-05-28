package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.company.model.dao.CompanyAdminMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperPing {
    private final CompanyAdminMapper mapper;

    @PostConstruct
    public void trigger() {
        Company dummy = new Company();
        dummy.setBusinessNo("1234567890");
        mapper.toCompanyAdminView(dummy);
    }
}
