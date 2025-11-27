package org.example.springecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Data
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIX: Field renamed from 'active' to 'archived'
    private boolean archived = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    private String color;
    private String size;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
}