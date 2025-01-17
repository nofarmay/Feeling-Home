package org.example.service;

import org.example.model.CommunityActivityScore;
import org.example.repository.CommunityActivityScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityActivityScoreService {
    private final CommunityActivityScoreRepository scoreRepository;

    @Autowired
    public CommunityActivityScoreService(CommunityActivityScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public List<CommunityActivityScore> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<CommunityActivityScore> getTopActiveUsers() {
        return scoreRepository.findAllByOrderByScorePointsDesc();
    }

    public List<CommunityActivityScore> getLowEngagementUsers(int threshold) {
        return scoreRepository.findByScorePointsGreaterThan(threshold);
    }

    public void resetAllScores() {
        List<CommunityActivityScore> allScores = scoreRepository.findAll();
        allScores.forEach(CommunityActivityScore::resetScore);
        scoreRepository.saveAll(allScores);
    }
}