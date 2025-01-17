package org.example.controller;

import org.example.model.CommunityActivityScore;
import org.example.service.CommunityActivityScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-scores")
public class CommunityActivityScoreController {
    private final CommunityActivityScoreService scoreService;

    @Autowired
    public CommunityActivityScoreController(CommunityActivityScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public ResponseEntity<List<CommunityActivityScore>> getAllScores() {
        return ResponseEntity.ok(scoreService.getAllScores());
    }

    @GetMapping("/top-active")
    public ResponseEntity<List<CommunityActivityScore>> getTopActiveUsers() {
        return ResponseEntity.ok(scoreService.getTopActiveUsers());
    }

    @GetMapping("/low-engagement")
    public ResponseEntity<List<CommunityActivityScore>> getLowEngagementUsers(
            @RequestParam(defaultValue = "5") int threshold) {
        return ResponseEntity.ok(scoreService.getLowEngagementUsers(threshold));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetAllScores() {
        scoreService.resetAllScores();
        return ResponseEntity.ok().build();
    }
}
