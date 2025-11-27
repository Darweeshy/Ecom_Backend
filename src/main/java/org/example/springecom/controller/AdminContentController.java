package org.example.springecom.controller;

import org.example.springecom.model.ContentPage;
import org.example.springecom.service.ContentPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pages")
public class AdminContentController {

    @Autowired
    private ContentPageService pageService;

    @GetMapping
    public List<ContentPage> getAllPages() {
        return pageService.getAllPages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentPage> getPageById(@PathVariable Long id) {
        return pageService.getPageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContentPage> createPage(@RequestBody ContentPage page) {
        ContentPage createdPage = pageService.createPage(page);
        return new ResponseEntity<>(createdPage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentPage> updatePage(@PathVariable Long id, @RequestBody ContentPage pageDetails) {
        ContentPage updatedPage = pageService.updatePage(id, pageDetails);
        return ResponseEntity.ok(updatedPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
        return ResponseEntity.noContent().build();
    }
}