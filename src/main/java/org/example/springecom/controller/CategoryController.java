package org.example.springecom.controller;

import org.example.springecom.model.Category;
import org.example.springecom.model.CategoryNodeDTO;
import org.example.springecom.model.ProductSummaryDTO;
import org.example.springecom.service.CategoryService;
import org.example.springecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // FIX: This endpoint is now specifically for the nested tree (e.g., for the
    // navbar).
    @GetMapping
    public ResponseEntity<List<CategoryNodeDTO>> getPublicCategoryTree() {
        return new ResponseEntity<>(categoryService.getPublicCategoryTree(), HttpStatus.OK);
    }

    // FIX: New endpoint dedicated to fetching only top-level categories for the
    // homepage.
    @GetMapping("/toplevel")
    public ResponseEntity<List<Category>> getTopLevelCategories() {
        return new ResponseEntity<>(categoryService.getTopLevelCategories(), HttpStatus.OK);
    }

    // DEBUG: Temporary endpoint to check all categories and their status
    @GetMapping("/debug/all")
    public ResponseEntity<List<Map<String, Object>>> debugAllCategories() {
        List<Category> allCategories = categoryService.getAllCategoriesForDebug();
        List<Map<String, Object>> debugInfo = allCategories.stream()
                .map(c -> Map.of(
                        "id", c.getId(),
                        "name", c.getName(),
                        "archived", c.isArchived(),
                        "parentId", c.getParent() != null ? c.getParent().getId() : "NULL"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(debugInfo);
    }

    // FIX: This endpoint now uses the dedicated public service method.
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getPublicCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductSummaryDTO>> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false, defaultValue = "default") String sort,
            @RequestParam(required = false, defaultValue = "0") int page) {
        Page<ProductSummaryDTO> products = productService.searchAndFilterProducts(id, brands, sizes, sort, page);
        return ResponseEntity.ok(products);
    }
}