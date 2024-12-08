package com.laterna.xaxathonprime.regionnews;

import com.laterna.xaxathonprime.regionnews.dto.RegionNewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/regions/{regionId}/news")
@RequiredArgsConstructor
public class RegionNewsController {

    private final RegionNewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegionNews> createNews(
            @PathVariable Long regionId,
            @RequestPart("news") RegionNewsDto newsDto,
            @RequestPart(value = "previewImage", required = false) MultipartFile previewImage) {
        return ResponseEntity.ok(newsService.createNews(regionId, newsDto, previewImage));
    }

    @GetMapping
    public ResponseEntity<List<RegionNews>> getRegionNews(@PathVariable Long regionId) {
        return ResponseEntity.ok(newsService.getRegionNews(regionId));
    }

    @PutMapping(value = "/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegionNews> updateNews(
            @PathVariable Long newsId,
            @RequestPart("news") RegionNewsDto newsDto,
            @RequestPart(value = "previewImage", required = false) MultipartFile previewImage) {
        return ResponseEntity.ok(newsService.updateNews(newsId, newsDto, previewImage));
    }

    @DeleteMapping("/{newsId}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long newsId) {
        newsService.deleteNews(newsId);
        return ResponseEntity.noContent().build();
    }
}
