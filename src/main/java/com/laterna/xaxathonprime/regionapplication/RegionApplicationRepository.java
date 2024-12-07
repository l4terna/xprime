package com.laterna.xaxathonprime.regionapplication;

import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RegionApplicationRepository extends JpaRepository<RegionApplication, Long> {
    Page<RegionApplication> findByRegionOrderByCreatedAtDesc(Region region, Pageable pageable);
    Page<RegionApplication> findByApplicantOrderByCreatedAtDesc(User applicant, Pageable pageable);
}
