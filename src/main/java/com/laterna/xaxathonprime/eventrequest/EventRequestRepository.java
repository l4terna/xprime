package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    Page<EventRequest> findAllByStatus(EventRequestStatus status, Pageable pageable);
}
