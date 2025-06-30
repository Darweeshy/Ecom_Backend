package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductSummaryDTO {
    private int id;
    private String name;
    private String brand;
    private String categoryName;
    private BigDecimal basePrice;
    private int stockQuantity;
    private String imageUrl; // <-- ADD THIS FIELD

    public ProductSummaryDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.stockQuantity = product.getStockQuantity();
        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
        }
        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            // Assumes the first variant's price and image are the main ones for the list view
            this.basePrice = product.getVariants().get(0).getPrice();
            this.imageUrl = product.getVariants().get(0).getImageUrl(); // <-- ADD THIS LINE
        }
    }
}