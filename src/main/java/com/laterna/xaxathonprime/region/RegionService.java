package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime._shared.context.UserContext;
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
        if(!userContext.getCurrentUser().role().name().equals(RoleType.FSP_ADMIN.name())) {
            throw new AccessDeniedException("You do not have permission to update this region");
        }

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + id));

        region.setName(regionDto.name());
        region.setContactEmail(regionDto.contactEmail());
        region.setDescription(regionDto.description());

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
}
