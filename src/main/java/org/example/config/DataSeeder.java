package org.example.config;

import org.example.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    // הגדרת סוגי הקבצים שאנחנו תומכים בהם
    private static final String[] SUPPORTED_EXTENSIONS = {
            ".jpg", ".jpeg", ".png",  // תמונות
            ".mp4", ".mov", ".avi"    // וידאו
    };

    // קריאת הנתיב המקורי מה-application.properties
    @Value("${app.initial.images.dir:src/main/resources/STATIC/img}")
    private String initialImagesDir;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public void run(String... args) throws Exception {
        // קריאה לפונקציה שמעתיקה את הקבצים ברגע שהאפליקציה עולה
        migrateExistingFiles();
    }

    // בודק אם סוג הקובץ נתמך
    private boolean isSupportedFile(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        return Arrays.stream(SUPPORTED_EXTENSIONS)
                .anyMatch(ext -> lowerFileName.endsWith(ext));
    }

    // פונקציית המיגרציה הראשית
    private void migrateExistingFiles() {
        try {
            Path sourceDir = Paths.get(initialImagesDir);
            if (Files.exists(sourceDir)) {
                System.out.println("Starting file migration from: " + sourceDir);

                Files.walk(sourceDir)  // עובר על כל הקבצים בתיקייה באופן רקורסיבי
                        .filter(Files::isRegularFile)  // מסנן רק קבצים (לא תיקיות)
                        .filter(path -> isSupportedFile(path.getFileName().toString()))  // מסנן רק קבצים נתמכים
                        .forEach(this::copyFile);  // מעתיק כל קובץ שעבר את הסינון

                System.out.println("File migration completed successfully");
            } else {
                System.out.println("Source directory not found: " + sourceDir);
            }
        } catch (IOException e) {
            System.err.println("Error during file migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // פונקציית ההעתקה של קובץ בודד
    private void copyFile(Path source) {
        try {
            String fileName = source.getFileName().toString();
            Path uploadDir = Paths.get(System.getProperty("user.home"), "uploads", "images");
            Path targetPath = uploadDir.resolve(fileName);

            // יוצר את תיקיית היעד אם היא לא קיימת
            Files.createDirectories(uploadDir);

            // בודק את גודל הקובץ אם זה וידאו
            if (fileName.toLowerCase().matches(".*\\.(mp4|mov|avi)$")) {
                long fileSize = Files.size(source);
                if (fileSize > 100_000_000) { // 100MB
                    System.err.println("Skipping large video file: " + fileName);
                    return;
                }
            }

            // מעתיק את הקובץ
            Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Successfully copied: " + fileName);

        } catch (IOException e) {
            System.err.println("Error copying file " + source.getFileName() + ": " + e.getMessage());
        }
    }
}