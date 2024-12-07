package com.laterna.xaxathonprime.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByRegionId(Long regionId);
}