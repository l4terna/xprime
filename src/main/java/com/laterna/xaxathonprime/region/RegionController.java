package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime.region.dto.CreateRegionDto;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.region.dto.UpdateRegionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor

public class RegionController {
    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<Page<RegionDto>> getAllRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "createdAt");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<RegionDto> regions = regionService.findAll(search, pageRequest);
        return ResponseEntity.ok(regions);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FSP_ADMIN')")
    public ResponseEntity<RegionDto> createRegion(@RequestBody CreateRegionDto createRegionDto) {
        return ResponseEntity.ok(regionService.createRegion(createRegionDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegion(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegion(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody UpdateRegionDto regionDto) {
        return ResponseEntity.ok(regionService.updateRegion(id, regionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
