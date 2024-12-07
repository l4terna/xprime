package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    @Query("SELECT er FROM EventRequest er WHERE er.status = :status AND er.status != 'APPROVED'")
    Page<EventRequest> findAllByStatus(@Param("status") EventRequestStatus status, Pageable pageable);

    @Query("SELECT er FROM EventRequest er WHERE er.status != 'APPROVED'")
    Page<EventRequest> findAll(Pageable pageable);
}
