package com.multi.matchingbot.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/")
    public String showCompanyHome(Model model) {
        return "/company/index"; // templates/index.html 렌더링
    }
}
