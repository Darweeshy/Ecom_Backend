package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryFlatDTO {
    private Long id;
    private String name;

    public CategoryFlatDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}