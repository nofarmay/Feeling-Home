package org.example.controller;

import org.example.DTO.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // מאפשר גישה מכל דומיין
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@ModelAttribute UserDTO userDTO) {
        try {
            System.out.println("Received user data:");
            System.out.println("Username: " + userDTO.getUsername());
            System.out.println("Email: " + userDTO.getEmail());
            System.out.println("Phone: " + userDTO.getPhoneNumber());
            System.out.println("Address: " + userDTO.getAddress());
            System.out.println("Has profile picture: " + (userDTO.getProfilePictureFile() != null));

            if (userDTO.getUsername() == null || userDTO.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Username and email are required");
            }

            // בדיקה אם קיים משתמש עם אותו אימייל
            if (userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("משתמש עם אימייל זה כבר קיים במערכת");
            }

            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("שגיאה ביצירת משתמש: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(id, user);
        return updatedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // נקודת קצה מאוחדת לעדכון תמונת פרופיל
    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            User updatedUser = userService.updateProfilePicture(id, file);
            // יצירת DTO עם הנתיב המלא לתמונה
            UserDTO responseDTO = new UserDTO();
            responseDTO.setUserId(updatedUser.getUserId());
            responseDTO.setUsername(updatedUser.getUsername());
            responseDTO.setEmail(updatedUser.getEmail());
            responseDTO.setPhoneNumber(updatedUser.getPhoneNumber());
            responseDTO.setAddress(updatedUser.getAddress());

            // הוספת נתיב מלא לתמונת הפרופיל
            if (updatedUser.getProfilePicture() != null) {
                responseDTO.setProfilePicture("/uploads/" + updatedUser.getProfilePicture());
            }

            return ResponseEntity.ok(responseDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update profile picture: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found or other error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long id) {
        try {
            userService.deleteProfilePicture(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}