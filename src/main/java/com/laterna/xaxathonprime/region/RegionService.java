package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime._shared.context.UserContext;
import com.laterna.xaxathonprime.region.dto.CreateRegionDto;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.region.dto.UpdateRegionDto;
import com.laterna.xaxathonprime.role.enumeration.RoleType;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public Page<RegionDto> findAll(String search, Pageable pageable) {
        Page<Region> regionsPage;

        if (StringUtils.hasText(search)) {
            regionsPage = regionRepository.findAllByNameContainingIgnoreCase(search, pageable);
        } else {
            regionsPage = regionRepository.findAll(pageable);
        }

        return regionsPage.map(regionMapper::toDto);
    }

    public List<RegionDto> findAll() {
        return regionRepository.findAll().stream().map(regionMapper::toDto).toList();
    }

    public RegionDto findById(Long id) {
        return regionRepository.findById(id)
                .map(regionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));
    }

    @Transactional(readOnly = true)
    public RegionDto getRegion(Long id) {
        return regionRepository.findById(id)
                .map(regionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));
    }

    @Transactional
    public RegionDto updateRegion(Long id, UpdateRegionDto regionDto) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));

        if(regionDto.name() != null) {
            region.setName(regionDto.name());
        }

        if(regionDto.contactEmail() != null) {
            region.setContactEmail(regionDto.contactEmail());  // Исправлено
        }

        if(regionDto.description() != null) {
            region.setDescription(regionDto.description());    // Исправлено
        }

        if (regionDto.userId() != null) {
            UserDto user = userService.findById(regionDto.userId());

            regionRepository.findByUserId(regionDto.userId())
                    .ifPresent(existingRegion -> {
                        if (!existingRegion.getId().equals(id)) {
                            throw new IllegalStateException("User is already assigned to another region");
                        }
                    });

            region.setUser(userMapper.toEntity(user));
        }

        return regionMapper.toDto(regionRepository.save(region));
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('FSP_ADMIN')")
    public void deleteRegion(Long id) {
        if (!userContext.getCurrentUser().role().name().equals(RoleType.FSP_ADMIN.name())) {
            throw new AccessDeniedException("You do not have permission to delete this region");
        }

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));

        if (region.getUser() != null) {
            region.setUser(null);
            regionRepository.save(region);
        }

        regionRepository.delete(region);
    }

    @Transactional
    @PreAuthorize("hasAuthority('FSP_ADMIN')")
    public RegionDto createRegion(CreateRegionDto createRegionDto) {
        if (createRegionDto.userId() != null) {
            regionRepository.findByUserId(createRegionDto.userId())
                    .ifPresent(region -> {
                        throw new IllegalStateException("User is already assigned to another region");
                    });
        }

        Region region = Region.builder()
                .name(createRegionDto.name())
                .description(createRegionDto.description())
                .contactEmail(createRegionDto.contactEmail())
                .imageUrl(createRegionDto.imageUrl())
                .user(createRegionDto.userId() != null ?
                        userMapper.toEntity(userService.findById(createRegionDto.userId())) :
                        null)
                .build();

        return regionMapper.toDto(regionRepository.save(region));
    }

    public long count() {
        return regionRepository.count();
    }

    public RegionDto save(Region build) {
        return regionMapper.toDto(regionRepository.save(build));
    }
}
