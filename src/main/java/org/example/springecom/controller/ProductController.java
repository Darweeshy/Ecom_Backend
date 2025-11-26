package org.example.springecom.controller;

import org.example.springecom.model.Product;
import org.example.springecom.model.ProductSummaryDTO;
import org.example.springecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // This is the main public endpoint for viewing products.
    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false, defaultValue = "default") String sort) {
        List<ProductSummaryDTO> products = productService.searchAndFilterProducts(categoryId, brands, sizes, sort);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/categories/{categoryId}/products")
public ResponseEntity<List<ProductSummaryDTO>> getProductsByCategory(
        @PathVariable Long categoryId,
        @RequestParam(required = false) List<String> brands,
        @RequestParam(required = false) List<String> sizes,
        @RequestParam(required = false, defaultValue = "default") String sort) {
    List<ProductSummaryDTO> products = productService.searchAndFilterProducts(categoryId, brands, sizes, sort);
    return ResponseEntity.ok(products);
}


    // This is the public endpoint for fetching a single product's details.
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return productService.getPublicProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/filters/brands")
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(productService.getDistinctBrands());
    }

    @GetMapping("/filters/sizes")
    public ResponseEntity<List<String>> getSizes() {
        return ResponseEntity.ok(productService.getDistinctSizes());
    }
}
