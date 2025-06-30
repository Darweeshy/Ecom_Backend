package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private int productId; // <-- ADD THIS LINE
    private Long variantId; // <-- ADD THIS LINE
    private String productName;
    private String variantColor;
    private String variantSize;
    private int quantity;
    private BigDecimal price;

    public OrderItemDTO(OrderItem orderItem) {
        ProductVariant variant = orderItem.getVariant();
        if (variant != null) {
            this.variantId = variant.getId(); // <-- ADD THIS LINE
            this.variantColor = variant.getColor();
            this.variantSize = variant.getSize();
            if (variant.getProduct() != null) {
                this.productId = variant.getProduct().getId(); // <-- ADD THIS LINE
                this.productName = variant.getProduct().getName();
            }
        }
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}