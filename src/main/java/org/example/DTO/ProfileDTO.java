package org.example.DTO;

public class ProfileDTO {
    private Long id;
    private String name;
    private String bio;
    private String location;
    private String profilePicture;
    private Long userId;

    public ProfileDTO() {
    }

    public ProfileDTO(Long id, String name, String bio, String location, String profilePicture, Long userId) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.profilePicture = profilePicture;
        this.userId = userId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}