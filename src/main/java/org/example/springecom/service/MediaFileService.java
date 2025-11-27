package org.example.springecom.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.springecom.model.MediaFile;
import org.example.springecom.repo.MediaFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepo mediaFileRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public List<MediaFile> uploadFiles(List<MultipartFile> files, String folder) throws IOException {
        List<MediaFile> uploadedFiles = new ArrayList<>();
        String normalizedFolder = folder.trim().toLowerCase();

        for (MultipartFile file : files) {
            if (file.isEmpty())
                continue;

            // Save the file using FileStorageService
            String savedFilename = fileStorageService.saveFile(file, normalizedFolder);

            // Create MediaFile entity
            MediaFile mediaFile = new MediaFile();
            mediaFile.setFilename(savedFilename);
            mediaFile.setOriginalFilename(file.getOriginalFilename());
            mediaFile.setFolder(normalizedFolder);
            mediaFile.setUrl("/images/" + normalizedFolder + "/" + savedFilename);
            mediaFile.setFileSize(file.getSize());
            mediaFile.setMimeType(file.getContentType());
            mediaFile.setIsPublic(false);

            uploadedFiles.add(mediaFileRepo.save(mediaFile));
        }

        return uploadedFiles;
    }

    @Transactional(readOnly = true)
    public Page<MediaFile> getMediaFiles(String folder, Pageable pageable) {
        if (folder != null && !folder.isEmpty()) {
            return mediaFileRepo.findByFolder(folder, pageable);
        }
        return mediaFileRepo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<String> getFolders() {
        return mediaFileRepo.findDistinctFolders();
    }

    @Transactional
    public void deleteFile(Long id) throws IOException {
        MediaFile mediaFile = mediaFileRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media file not found with id: " + id));

        // Delete the physical file
        Path filePath = Paths.get(fileStorageService.getUploadDir())
                .resolve(mediaFile.getFolder())
                .resolve(mediaFile.getFilename());

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete the database record
        mediaFileRepo.delete(mediaFile);
    }

    @Transactional
    public MediaFile makePublic(Long id) {
        MediaFile mediaFile = mediaFileRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media file not found with id: " + id));
        mediaFile.setIsPublic(true);
        return mediaFileRepo.save(mediaFile);
    }
}
