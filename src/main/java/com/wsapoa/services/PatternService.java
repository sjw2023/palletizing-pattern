package com.wsapoa.services;

import com.wsapoa.entity.Pattern;
import com.wsapoa.repository.PatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternService {

    private final PatternRepository patternRepository;

    public Pattern getPattern(Long id) {
        return patternRepository.findById(id).orElseThrow(() -> new RuntimeException("Pattern not found"));
    }

    public List<Pattern> getAllPatterns() {
        return patternRepository.findAll();
    }
}