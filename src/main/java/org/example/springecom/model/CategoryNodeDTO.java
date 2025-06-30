package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CategoryNodeDTO {
    private Long id;
    private String name;
    private Set<CategoryNodeDTO> children;

    public CategoryNodeDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            // FIX: Recursively map children, but only if they are not archived.
            this.children = category.getChildren().stream()
                    .filter(child -> !child.isArchived())
                    .map(CategoryNodeDTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
