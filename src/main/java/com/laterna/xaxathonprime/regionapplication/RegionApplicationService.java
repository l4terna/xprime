package com.laterna.xaxathonprime.regionapplication;

import com.laterna.xaxathonprime._shared.context.UserContext;
import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.regionapplication.dto.CreateApplicationRequest;
import com.laterna.xaxathonprime.regionapplication.dto.ProcessApplicationRequest;
import com.laterna.xaxathonprime.regionapplication.enumeration.ApplicationStatus;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegionApplicationService {
    private final RegionApplicationRepository applicationRepository;
    private final RegionService regionService;
    private final RegionMapper regionMapper;
    private final UserMapper userMapper;
    private final UserContext userContext;

    @Transactional
    public RegionApplication createApplication(CreateApplicationRequest request) {
        RegionDto region = regionService.findById(request.regionId());
        User user = userMapper.toEntity(userContext.getCurrentUser());

        RegionApplication application = RegionApplication.builder()
                .region(regionMapper.toEntity(region))
                .applicant(user)
                .title(request.title())
                .description(request.description())
                .status(ApplicationStatus.PENDING)
                .build();

        return applicationRepository.save(application);
    }

    @Transactional
    public RegionApplication processApplication(Long applicationId, ProcessApplicationRequest request) {
        RegionApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(request.status());
        application.setResponseMessage(request.responseMessage());

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public Page<RegionApplication> getApplicationsByRegion(Pageable pageable) {
        Region region = regionMapper.toEntity(userContext.getCurrentUser().region());
        return applicationRepository.findByRegionOrderByCreatedAtDesc(region, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RegionApplication> getApplicationsByApplicant(Pageable pageable) {
        User user = userMapper.toEntity(userContext.getCurrentUser());
        return applicationRepository.findByApplicantOrderByCreatedAtDesc(user, pageable);
    }

    @Transactional(readOnly = true)
    public RegionApplication getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
}