package org.example.springecom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springecom.model.Product;
import org.example.springecom.model.ProductVariant;
import org.example.springecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products") // This path is protected by SecurityConfig
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") String productStr,
            @RequestPart("variants") String variantsStr,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        Product product = objectMapper.readValue(productStr, Product.class);
        List<ProductVariant> variants = objectMapper.readValue(variantsStr, new TypeReference<>() {});
        Product savedProduct = productService.addProduct(product, variants, images);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestPart("product") String productStr,
            @RequestPart("variants") String variantsStr,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        Product productDetails = objectMapper.readValue(productStr, Product.class);
        List<ProductVariant> variantsDetails = objectMapper.readValue(variantsStr, new TypeReference<>() {});
        Product updatedProduct = productService.updateProduct(id, productDetails, variantsDetails, images);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        // FIX: This now calls the public-safe method which checks the 'archived' flag.
        return productService.getPublicProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        // FIX: This now calls the archiveProduct method instead of delete.
        productService.archiveProduct(id);
        return ResponseEntity.noContent().build();
    }
}