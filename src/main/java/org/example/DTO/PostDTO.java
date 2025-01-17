package org.example.DTO;

import java.util.Date;

public class PostDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private boolean anonymous;
    private Long userId;

    public PostDTO() {
    }

    public PostDTO(Long id, String content, Date createdAt, boolean anonymous, Long userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.anonymous = anonymous;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}