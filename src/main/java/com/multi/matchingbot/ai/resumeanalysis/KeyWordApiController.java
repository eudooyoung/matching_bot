package com.multi.matchingbot.ai.resumeanalysis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keyword")
@RequiredArgsConstructor
public class KeyWordApiController {

    private final KeywordExtractor keywordExtractor;

    @PostMapping("/extract")
    public List<String> extractKeyword(@RequestParam String text) {
        return keywordExtractor.extractKeywords(text);
    }
}
