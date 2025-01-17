package org.example.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileSystemUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileStorageServiceTest {

    @Autowired
    private FileStorageService service;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test-uploads");
        ReflectionTestUtils.setField(service, "uploadDir", tempDir.toString());
    }

    @AfterEach
    void cleanup() throws IOException {
        FileSystemUtils.deleteRecursively(tempDir);
    }

    @Test
    void shouldStoreValidImageFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        String fileName = service.storeFile(file);

        assertNotNull(fileName);
        assertTrue(fileName.endsWith(".jpg"));
        assertTrue(Files.exists(tempDir.resolve(fileName)));
    }

    @Test
    void shouldRejectOversizedImage() {
        byte[] largeContent = new byte[6_000_000];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                largeContent
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.storeFile(file);
        });

        assertEquals("תמונה גדולה מדי. גודל מקסימלי: 5MB", exception.getMessage());
    }

    @Test
    void shouldRejectUnsupportedFileType() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.storeFile(file);
        });

        assertEquals("סוג קובץ לא נתמך: text/plain", exception.getMessage());
    }

    @Test
    void shouldDeleteExistingFile() throws IOException {
        String fileName = "test-file.jpg";
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, "test content".getBytes());

        service.deleteFile(fileName);

        assertFalse(Files.exists(filePath));
    }

    @Test
    void shouldThrowWhenDeletingNonExistentFile() {
        String nonExistentFile = "non-existent.jpg";

        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            service.deleteFile(nonExistentFile);
        });

        assertEquals("הקובץ לא נמצא: " + nonExistentFile, exception.getMessage());
    }
}