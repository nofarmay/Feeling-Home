package org.example.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

// TODO: Authentication Update Required
/*
אחרי הוספת מערכת אימות צריך:
1. להוסיף בדיקת הרשאות בקונטרולר
2. לוודא שמשתמש יכול לעדכן רק את התמונה שלו
3. להוסיף טיפול בסשן
*/

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber = "";

    @Column(name = "isCoordinator", nullable = false)
    private boolean isCoordinator = false;

    @Column(name = "registrationCode", nullable = false)
    private String registrationCode = "USER_DEFAULT";

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


//    @OneToMany(mappedBy = "user")
//    private Set<Event> createdEvents = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    private Set<Post> posts = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "participants")
    private Set<Event> attendingEvents = new HashSet<>();

    public User() {
        this.createdAt = new Date();
    }

    public User(String username, String email, String registrationCode, String phoneNumber, String address) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        setRegistrationCode(registrationCode);
        this.createdAt = new Date();
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Set<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public boolean isAdmin() {
        return this.isCoordinator;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsCoordinator(boolean isCoordinator) {
        this.isCoordinator = isCoordinator;
    }

    public void setRegistrationCode(String code) {
        this.registrationCode = code;
        this.isCoordinator = "COORD123".equals(code);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

//    public void setCreatedEvents(Set<Event> createdEvents) {
//        this.createdEvents = createdEvents;
//    }
//
//    public void setPosts(Set<Post> posts) {
//        this.posts = posts;
//    }

    //    public Set<Event> getCreatedEvents() {
//        return createdEvents;
//    }
//
//    public Set<Post> getPosts() {
//        return posts;
//    }

    public void setAttendingEvents(Set<Event> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }
}