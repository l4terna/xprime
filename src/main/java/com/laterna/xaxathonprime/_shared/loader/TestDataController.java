package com.laterna.xaxathonprime._shared.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-data")
@RequiredArgsConstructor
public class TestDataController {
    
    private final TestDataLoader dataLoader;
    
    @PostMapping("/load")
    public ResponseEntity<String> loadTestData() {
        dataLoader.run();  // или создайте отдельный метод
        return ResponseEntity.ok("Test data loaded");
    }
}