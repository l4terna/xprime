package com.laterna.xaxathonprime.eventprotocol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface EventProtocolRepository extends JpaRepository<EventProtocol, Long> {
    Optional<EventProtocol> findByEventBaseIdAndRegionId(Long eventBaseId, Long regionId);
}