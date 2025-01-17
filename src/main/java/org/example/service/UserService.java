package org.example.service;

import org.springframework.web.multipart.MultipartFile;
import org.example.DTO.UserDTO;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(UserDTO userDTO) {
        // בדיקה אם כבר קיים משתמש עם אותו אימייל
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("משתמש עם אימייל זה כבר קיים במערכת");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());

        // TODO: להוריד כשתהיה מערכת אוטוריזציה
        // // קביעת ערכי ברירת מחדל
        // user.setRegistrationCode("USER" + System.currentTimeMillis());
        // user.setIsCoordinator(false);

        // אם יש תמונת פרופיל בDTO
        if (userDTO.getProfilePictureFile() != null && !userDTO.getProfilePictureFile().isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(userDTO.getProfilePictureFile());
                user.setProfilePicture(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store profile picture", e);
            }
        }

        return userRepository.save(user);
    }

    // אפשר גם להוסיף מתודת עזר להמרה מ-DTO ל-User
    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        return user;
    }

    public Optional<User> updateUser(Long id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setAddress(user.getAddress());

            // TODO: להוריד כשתהיה מערכת אוטוריזציה
            // existingUser.setRegistrationCode(user.getRegistrationCode());
            // existingUser.setIsCoordinator(user.isCoordinator());

            return userRepository.save(existingUser);
        });
    }

    public User updateProfilePicture(Long userId, MultipartFile file) throws IOException {
        return userRepository.findById(userId).map(user -> {
            try {
                // מחיקת תמונה קודמת אם קיימת
                if (user.getProfilePicture() != null) {
                    fileStorageService.deleteFile(user.getProfilePicture());
                }

                // שמירת התמונה החדשה
                String fileName = fileStorageService.storeFile(file);
                user.setProfilePicture(fileName);

                return userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update profile picture", e);
            }
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteProfilePicture(Long userId) throws IOException {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getProfilePicture() != null) {
                try {
                    fileStorageService.deleteFile(user.getProfilePicture());
                    user.setProfilePicture(null);
                    userRepository.save(user);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete profile picture", e);
                }
            }
        });
    }
}