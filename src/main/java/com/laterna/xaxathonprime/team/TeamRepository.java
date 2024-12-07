package com.laterna.xaxathonprime.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface TeamRepository extends JpaRepository<Team, Long> {
    Page<Team> findByRegionId(Long regionId, Pageable pageable);

}