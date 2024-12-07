package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    Page<EventRequest> findAllByStatus(EventRequestStatus status, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN FETCH er.base b WHERE b.region.id = :regionId AND er.status = :status")
    Page<EventRequest> findAllByBaseRegionIdAndStatus(Long regionId, EventRequestStatus status, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN FETCH er.base b WHERE b.region.id = :regionId")
    Page<EventRequest> findAllByBaseRegionId(Long regionId, Pageable pageable);
}
