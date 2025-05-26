package com.multi.matchingbot.map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/map_popup")
    public String mapPopup() {
        return "main/map_popup";
    }

//    @GetMapping("/main")
//    public String mainPage() {
//        return "main/main";
//    }
}
