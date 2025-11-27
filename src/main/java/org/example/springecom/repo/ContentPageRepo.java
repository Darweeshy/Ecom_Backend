package org.example.springecom.repo;

import org.example.springecom.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentPageRepo extends JpaRepository<ContentPage, Long> {
    Optional<ContentPage> findBySlug(String slug);
}