package com.laterna.xaxathonprime.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.base b " +
            "LEFT JOIN FETCH b.region " +
            "LEFT JOIN FETCH b.disciplines " +
            "WHERE b.startDate IS NOT NULL " +
            "AND b.startDate > :currentDate " +
            "ORDER BY b.startDate ASC")
    List<Event> findConfirmedUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);
}
