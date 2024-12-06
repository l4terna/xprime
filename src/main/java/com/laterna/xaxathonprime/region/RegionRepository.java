package com.laterna.xaxathonprime.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface RegionRepository extends JpaRepository<Region, Long> {
    Page<Region> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Region> findByUserId(Long userId);
}
