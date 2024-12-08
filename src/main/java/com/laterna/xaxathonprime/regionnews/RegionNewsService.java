package com.laterna.xaxathonprime.regionnews;

import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.regionnews.dto.RegionNewsDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegionNewsService {

    private final RegionNewsRepository newsRepository;
    private final RegionService regionService;
    private final RegionMapper regionMapper;
    
    @Value("${app.upload.news-images:news-images}")
    private String uploadPath;

    public RegionNews createNews(Long regionId, RegionNewsDto newsDto, MultipartFile previewImage) {
        RegionDto region = regionService.findById(regionId);

        String imagePath = null;
        if (previewImage != null && !previewImage.isEmpty()) {
            imagePath = saveImage(previewImage);
        }

        RegionNews news = RegionNews.builder()
                .title(newsDto.title())
                .content(newsDto.content())
                .previewImagePath(imagePath)
                .region(regionMapper.toEntity(region))
                .build();

        return newsRepository.save(news);
    }

    public List<RegionNews> getRegionNews(Long regionId) {
        return newsRepository.findByRegionIdOrderByCreatedAtDesc(regionId);
    }

    private String saveImage(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            Path destination = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), destination);
            
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public RegionNews updateNews(Long newsId, RegionNewsDto newsDto, MultipartFile previewImage) {
        RegionNews news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News not found"));

        news.setTitle(newsDto.title());
        news.setContent(newsDto.content());

        if (previewImage != null && !previewImage.isEmpty()) {
            // Delete old image if exists
            if (news.getPreviewImagePath() != null) {
                deleteImage(news.getPreviewImagePath());
            }
            // Save new image
            String imagePath = saveImage(previewImage);
            news.setPreviewImagePath(imagePath);
        }

        return newsRepository.save(news);
    }

    public void deleteNews(Long newsId) {
        RegionNews news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News not found"));

        // Delete associated image if exists
        if (news.getPreviewImagePath() != null) {
            deleteImage(news.getPreviewImagePath());
        }

        newsRepository.delete(news);
    }

    private void deleteImage(String filename) {
        try {
            Path file = Paths.get(uploadPath).resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}