package org.example.springecom.controller;

import org.example.springecom.model.AdminCategoryDTO;
import org.example.springecom.model.Category;
import org.example.springecom.model.CategoryFlatDTO;
import org.example.springecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<AdminCategoryDTO>> getCategoryTreeForAdmin() {
        return ResponseEntity.ok(categoryService.getAdminCategoryTree());
    }

    @GetMapping("/flat")
    public ResponseEntity<List<CategoryFlatDTO>> getCategoryFlatListForAdmin() {
        return ResponseEntity.ok(categoryService.getAdminCategoryFlatList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryByIdForAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryByIdForAdmin(id));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestPart("category") String categoryStr,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<>(categoryService.createOrUpdateCategory(null, categoryStr, image), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") String categoryStr,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(categoryService.createOrUpdateCategory(id, categoryStr, image));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> toggleArchiveCategory(@PathVariable Long id) {
        categoryService.toggleArchiveCategory(id);
        return ResponseEntity.noContent().build();
    }
}