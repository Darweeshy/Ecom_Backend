package org.example.springecom.controller;

import org.example.springecom.model.MediaFile;
import org.example.springecom.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/media")
public class AdminMediaController {

    @Autowired
    private MediaFileService mediaFileService;

    @GetMapping
    public ResponseEntity<Page<MediaFile>> getMediaFiles(
            @RequestParam(required = false) String folder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "uploadedAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<MediaFile> mediaFiles = mediaFileService.getMediaFiles(folder, pageable);
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/folders")
    public ResponseEntity<List<String>> getFolders() {
        List<String> folders = mediaFileService.getFolders();
        return ResponseEntity.ok(folders);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<MediaFile>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("folder") String folder) {
        try {
            List<MediaFile> uploadedFiles = mediaFileService.uploadFiles(files, folder);
            return new ResponseEntity<>(uploadedFiles, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            mediaFileService.deleteFile(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/make-public")
    public ResponseEntity<MediaFile> makePublic(@PathVariable Long id) {
        MediaFile mediaFile = mediaFileService.makePublic(id);
        return ResponseEntity.ok(mediaFile);
    }
}
