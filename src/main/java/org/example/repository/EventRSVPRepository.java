package org.example.repository;

import org.example.model.EventRSVP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRSVPRepository extends JpaRepository<EventRSVP, Long> {
    Optional<EventRSVP> findByEvent_EventIdAndUserId(Long eventId, Long userId);
}