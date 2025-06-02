package com.multi.matchingbot.chatbot;

import com.multi.matchingbot.company.domain.CompanyRegisterDto;

import java.util.HashMap;
import java.util.Map;

public class ReportDataBuilder {
    public static Map<String, Object> fromCompany(CompanyRegisterDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", dto.getName());
        map.put("year", dto.getEstablishedYear());
        map.put("headcount", dto.getHeadcount());
        map.put("industry", dto.getIndustry());
        map.put("revenue", dto.getAnnualRevenue());
        map.put("income", dto.getOperatingIncome());
        map.put("jobs", dto.getJobsLastYear());
        map.put("debt_note", "부채 정보는 제공되지 않음");
        return map;
    }
}
