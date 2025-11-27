package org.example.springecom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Inject the root upload directory from application.properties
    @Value("${file.upload-dir}")
    private String rootUploadDir;

    /**
     * Saves a file to a specified subdirectory within the root upload directory.
     * 
     * @param file         The MultipartFile to save.
     * @param subdirectory The name of the subdirectory (e.g., "products" or
     *                     "categories").
     * @return The unique filename of the saved file.
     * @throws IOException if the file is empty or cannot be saved.
     */
    public String saveFile(MultipartFile file, String subdirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // Create the full path: <rootUploadDir>/<subdirectory>
        Path targetDirectory = Paths.get(rootUploadDir, subdirectory);
        if (!Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate a unique filename to avoid collisions
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        Path destinationFile = targetDirectory.resolve(uniqueFilename).toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFilename;
    }

    public String getUploadDir() {
        return rootUploadDir;
    }
}