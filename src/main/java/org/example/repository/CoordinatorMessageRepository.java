package org.example.repository;

import org.example.model.CoordinatorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CoordinatorMessageRepository extends JpaRepository<CoordinatorMessage, Long> {
    List<CoordinatorMessage> findByRegistrationCode(String registrationCode);
}