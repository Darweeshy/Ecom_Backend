package org.example.springecom.controller;

import org.example.springecom.model.Category;
import org.example.springecom.model.CategoryNodeDTO;
import org.example.springecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // FIX: This endpoint is now specifically for the nested tree (e.g., for the navbar).
    @GetMapping
    public ResponseEntity<List<CategoryNodeDTO>> getPublicCategoryTree() {
        return new ResponseEntity<>(categoryService.getPublicCategoryTree(), HttpStatus.OK);
    }

    // FIX: New endpoint dedicated to fetching only top-level categories for the homepage.
    @GetMapping("/toplevel")
    public ResponseEntity<List<Category>> getTopLevelCategories() {
        return new ResponseEntity<>(categoryService.getTopLevelCategories(), HttpStatus.OK);
    }

    // FIX: This endpoint now uses the dedicated public service method.
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getPublicCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}