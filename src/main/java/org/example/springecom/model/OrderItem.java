package org.example.springecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString; // Import this

import java.math.BigDecimal;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id") // The foreign key column in the database
    private ProductVariant variant;

    private int quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    @ToString.Exclude // This breaks the infinite loop
    private Order order;
}