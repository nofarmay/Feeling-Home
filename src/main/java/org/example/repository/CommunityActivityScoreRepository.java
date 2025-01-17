package org.example.repository;

import org.example.model.CommunityActivityScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CommunityActivityScoreRepository extends JpaRepository<CommunityActivityScore, Long> {
    List<CommunityActivityScore> findByScorePointsGreaterThan(int threshold);
    List<CommunityActivityScore> findAllByOrderByScorePointsDesc();
    //Optional<CommunityActivityScore> findByRegistrationCode(String registrationCode);
}