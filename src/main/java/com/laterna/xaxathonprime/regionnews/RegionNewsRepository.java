package com.laterna.xaxathonprime.regionnews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionNewsRepository extends JpaRepository<RegionNews, Long> {
    List<RegionNews> findByRegionIdOrderByCreatedAtDesc(Long regionId);
}
