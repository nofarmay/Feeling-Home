package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // קבועים לוולידציה
    private static final long MAX_IMAGE_SIZE = 5_000_000; // 5MB
    private static final long MAX_VIDEO_SIZE = 100_000_000; // 100MB
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif"
    );
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4",
            "video/quicktime",
            "video/x-msvideo"
    );

    public String storeFile(MultipartFile file) throws IOException {
        validateFile(file);
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = generateUniqueFileName(file);
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("לא ניתן לזהות את סוג הקובץ");
        }

        // בדיקת קבצי תמונה
        if (contentType.startsWith("image/")) {
            if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                throw new IllegalArgumentException("סוג תמונה לא נתמך. סוגים מותרים: " +
                        String.join(", ", ALLOWED_IMAGE_TYPES));
            }
            if (file.getSize() > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException("תמונה גדולה מדי. גודל מקסימלי: " +
                        MAX_IMAGE_SIZE / 1_000_000 + "MB");
            }
        }
        // בדיקת קבצי וידאו
        else if (contentType.startsWith("video/")) {
            if (!ALLOWED_VIDEO_TYPES.contains(contentType)) {
                throw new IllegalArgumentException("סוג וידאו לא נתמך. סוגים מותרים: " +
                        String.join(", ", ALLOWED_VIDEO_TYPES));
            }
            if (file.getSize() > MAX_VIDEO_SIZE) {
                throw new IllegalArgumentException("וידאו גדול מדי. גודל מקסימלי: " +
                        MAX_VIDEO_SIZE / 1_000_000 + "MB");
            }
        }
        else {
            throw new IllegalArgumentException("סוג קובץ לא נתמך: " + contentType);
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("שם הקובץ המקורי הוא null");
        }

        String fileExtension = originalFileName.substring(
                originalFileName.lastIndexOf(".")).toLowerCase();
        return UUID.randomUUID().toString() + fileExtension;
    }

    public void deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("שם הקובץ לא יכול להיות ריק");
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (!Files.deleteIfExists(filePath)) {
            throw new FileNotFoundException("הקובץ לא נמצא: " + fileName);
        }
    }
}