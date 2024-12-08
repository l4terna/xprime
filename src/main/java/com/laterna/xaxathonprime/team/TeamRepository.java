package com.laterna.xaxathonprime.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
interface TeamRepository extends JpaRepository<Team, Long> {
    Page<Team> findByRegionId(Long regionId, Pageable pageable);

    @Query("SELECT t FROM Team t WHERE t.region.id = :regionId")
    List<Team> findAllByRegionId(@Param("regionId") Long regionId);

    @Query("SELECT COUNT(t) FROM Team t WHERE t.region.id = :regionId")
    int countTeamsByRegionId(@Param("regionId") Long regionId);

    @Query("SELECT COUNT(DISTINCT u) FROM Team t JOIN t.users u WHERE t.region.id = :regionId")
    int countParticipantsByRegionId(@Param("regionId") Long regionId);

}