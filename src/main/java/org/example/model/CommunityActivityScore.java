package org.example.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "COMMUNITY_ACTIVITY_SCORE")
public class CommunityActivityScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;

    @Column(name = "score_points", nullable = false)
    private int scorePoints;

    @Column(name = "calculated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date calculatedAt;

    @Column(name = "posts_count")
    private int postsCount = 0;

    @Column(name = "comments_count")
    private int commentsCount = 0;

    @Column(name = "events_rsvp_count")
    private int eventsRsvpCount = 0;

    @Column(name = "coordinator_messages_count")
    private int coordinatorMessagesCount = 0;

    @Column(name = "last_reset_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastResetDate;

    public CommunityActivityScore() {
        this.scorePoints = 0;
        this.calculatedAt = new Date();
        this.lastResetDate = new Date();
    }

    public void incrementPostCount() {
        this.postsCount++;
        addPoint();
    }

    public void incrementCommentCount() {
        this.commentsCount++;
        addPoint();
    }

    public void incrementEventRSVP() {
        this.eventsRsvpCount++;
        addPoint();
    }

    public void incrementCoordinatorMessage() {
        this.coordinatorMessagesCount++;
        addPoint();
    }

    private void addPoint() {
        this.scorePoints++;
        this.calculatedAt = new Date();
    }

    public void resetScore() {
        this.scorePoints = 0;
        this.postsCount = 0;
        this.commentsCount = 0;
        this.eventsRsvpCount = 0;
        this.coordinatorMessagesCount = 0;
        this.lastResetDate = new Date();
        this.calculatedAt = new Date();
    }

    // Getters and Setters
    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
        this.calculatedAt = new Date();
    }

    public Date getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Date calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getEventsRsvpCount() {
        return eventsRsvpCount;
    }

    public int getCoordinatorMessagesCount() {
        return coordinatorMessagesCount;
    }

    public Date getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(Date lastResetDate) {
        this.lastResetDate = lastResetDate;
    }
}