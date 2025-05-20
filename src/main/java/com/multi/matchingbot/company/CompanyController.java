package com.multi.matchingbot.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/")
    public String showCompanyHome(Model model) {
        Long companyId = 1L;

        model.addAttribute("company_name", companyDto.getName());

        return "/company/index"; // templates/index.html 렌더링
    }
}
