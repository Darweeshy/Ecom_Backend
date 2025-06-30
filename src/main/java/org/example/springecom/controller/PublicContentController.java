package org.example.springecom.controller;

import org.example.springecom.model.ContentPage;
import org.example.springecom.service.ContentPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
public class PublicContentController {

    @Autowired
    private ContentPageService pageService;

    @GetMapping("/{slug}")
    public ResponseEntity<ContentPage> getPageBySlug(@PathVariable String slug) {
        return pageService.getPageBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}