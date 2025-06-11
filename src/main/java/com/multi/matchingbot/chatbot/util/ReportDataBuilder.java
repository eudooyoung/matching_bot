package com.multi.matchingbot.chatbot.util;

import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import com.multi.matchingbot.company.domain.CompanyUpdateReportDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReportDataBuilder {
    public static Map<String, Object> fromCompany(CompanyRegisterDto dto) {
            return buildCommonMap(
                    dto.getName(),
                    dto.getYearFound(),
                    dto.getHeadcount(),
                    dto.getIndustry(),
                    dto.getAnnualRevenue(),
                    dto.getOperatingIncome(),
                    dto.getJobsLastYear()
            );
        }

        public static Map<String, Object> fromCompany(CompanyUpdateReportDto dto) {
            return buildCommonMap(
                    dto.getName(),
                    dto.getYearFound(),
                    dto.getHeadcount(),
                    dto.getIndustry(),
                    dto.getAnnualRevenue(),
                    dto.getOperatingIncome(),
                    dto.getJobsLastYear()
            );
        }

        private static Map<String, Object> buildCommonMap(String name, int year, int headcount,
                                                          String industry, int revenue, int income, int jobs) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("year", year);
            map.put("headcount", headcount);
            map.put("industry", industry);
            map.put("revenue", revenue);
            map.put("income", income);
            map.put("jobs", jobs);
            map.put("debt_note", "부채 정보는 제공되지 않음");
            return map;
        }
}
