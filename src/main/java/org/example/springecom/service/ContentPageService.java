package org.example.springecom.service;

import org.example.springecom.model.ContentPage;
import org.example.springecom.repo.ContentPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORT THIS

import java.util.List;
import java.util.Optional;

@Service
public class ContentPageService {

    @Autowired
    private ContentPageRepo contentPageRepo;

    // --- Public-facing methods ---
    @Transactional(readOnly = true) // <-- ADD THIS ANNOTATION
    public Optional<ContentPage> getPageBySlug(String slug) {
        return contentPageRepo.findBySlug(slug);
    }

    // --- Admin-facing methods ---
    public List<ContentPage> getAllPages() {
        return contentPageRepo.findAll();
    }

    public Optional<ContentPage> getPageById(Long id) {
        return contentPageRepo.findById(id);
    }

    public ContentPage createPage(ContentPage page) {
        page.setSlug(generateSlug(page.getTitle()));
        return contentPageRepo.save(page);
    }

    public ContentPage updatePage(Long id, ContentPage pageDetails) {
        ContentPage existingPage = contentPageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));

        existingPage.setTitle(pageDetails.getTitle());
        existingPage.setContent(pageDetails.getContent());
        existingPage.setSlug(generateSlug(pageDetails.getTitle()));

        return contentPageRepo.save(existingPage);
    }

    public void deletePage(Long id) {
        contentPageRepo.deleteById(id);
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("--+", "-")
                .replaceAll("^-|-$", "");
    }
}