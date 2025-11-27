package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AdminCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private boolean archived;
    private List<AdminCategoryDTO> children;

    public AdminCategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.imageUrl = category.getImageUrl();
        this.archived = category.isArchived();
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            this.children = category.getChildren().stream()
                    .map(AdminCategoryDTO::new)
                    .sorted(Comparator.comparing(AdminCategoryDTO::getName))
                    .collect(Collectors.toList());
        }
    }
}