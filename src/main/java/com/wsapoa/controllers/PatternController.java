package com.wsapoa.controllers;

import com.wsapoa.entity.Pattern;
import com.wsapoa.services.PatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patterns")
@RequiredArgsConstructor
public class PatternController {

    private final PatternService patternService;

    @GetMapping("/{id}")
    public Pattern getPattern(@PathVariable Long id) {
        return patternService.getPattern(id);
    }

    @GetMapping
    public List<Pattern> getAllPatterns() {
        return patternService.getAllPatterns();
    }
}