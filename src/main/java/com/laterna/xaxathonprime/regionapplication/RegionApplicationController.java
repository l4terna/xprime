package com.laterna.xaxathonprime.regionapplication;

import com.laterna.xaxathonprime.regionapplication.dto.CreateApplicationRequest;
import com.laterna.xaxathonprime.regionapplication.dto.ProcessApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/region-applications")
@RequiredArgsConstructor
public class RegionApplicationController {
    private final RegionApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<RegionApplication> createApplication(
            @RequestBody CreateApplicationRequest request
    ) {
        return ResponseEntity.ok(applicationService.createApplication(request));
    }

    @PutMapping("/{applicationId}/process")
    @PreAuthorize("hasAnyAuthority('REGION_ADMIN')")
    public ResponseEntity<RegionApplication> processApplication(
            @PathVariable Long applicationId,
            @RequestBody ProcessApplicationRequest request
    ) {
        return ResponseEntity.ok(applicationService.processApplication(applicationId, request));
    }

    @GetMapping("/region")
    @PreAuthorize("hasAnyAuthority('REGION_ADMIN')")
    public ResponseEntity<Page<RegionApplication>> getRegionApplications(
            Pageable pageable
    ) {
        return ResponseEntity.ok(applicationService.getApplicationsByRegion(pageable));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Page<RegionApplication>> getMyApplications(
            Pageable pageable
    ) {
        return ResponseEntity.ok(applicationService.getApplicationsByApplicant(pageable));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<RegionApplication> getApplication(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(applicationService.getApplication(applicationId));
    }
}
