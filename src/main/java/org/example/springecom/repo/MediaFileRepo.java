package org.example.springecom.repo;

import org.example.springecom.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepo extends JpaRepository<MediaFile, Long> {

    Page<MediaFile> findByFolder(String folder, Pageable pageable);

    @Query("SELECT DISTINCT m.folder FROM MediaFile m ORDER BY m.folder")
    List<String> findDistinctFolders();
}
