package org.example.springecom.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContentPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Lob
    @Column(nullable = false, length = 10000)
    private String content;

    // FIX: Field renamed from 'active' to 'archived'
    private boolean archived = false;
}
