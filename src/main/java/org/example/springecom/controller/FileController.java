package org.example.springecom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class FileController {

    // Inject the root upload directory from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/{subdirectory}/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String subdirectory, @PathVariable String filename) {
        try {
            // Construct the root path from our configuration property.
            Path baseDir = Paths.get(uploadDir).toAbsolutePath();
            Path subDir = baseDir.resolve(subdirectory);

            // Security check to prevent path traversal attacks (e.g., /images/../../etc/passwd)
            if (!subDir.normalize().startsWith(baseDir.normalize())) {
                return ResponseEntity.badRequest().build();
            }

            Path filePath = subDir.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Default content type
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                throw new FileNotFoundException("File not found: " + filePath);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}