package com.laterna.xaxathonprime.region;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class RegionDataImportController {
    private final RegionDataImportService importService;

    @PostMapping("/regions")
    public ResponseEntity<String> importRegions() {
        importService.importData();
        return ResponseEntity.ok("Import completed successfully");
    }
}
