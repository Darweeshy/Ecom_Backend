package org.example.springecom.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // FIX: Field renamed from 'active' to 'archived'
    private boolean archived = false;

    private String name;
    private String description;
    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private boolean productAvailable;
    private int stockQuantity;
    private Date releaseDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants;
}

