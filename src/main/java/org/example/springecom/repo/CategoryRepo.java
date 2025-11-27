package org.example.springecom.repo;

import org.example.springecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    // FIX: Renamed and clarified purpose. This gets ALL top-level categories for admin tree building.
    List<Category> findByParentIsNull();

    // FIX: New query to get only ACTIVE top-level categories for the public homepage.
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.archived = false")
    List<Category> findTopLevelActiveCategories();

    // FIX: New query to eagerly fetch the entire category tree for the public navbar.
    // This helps avoid lazy loading issues during DTO conversion.
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL AND c.archived = false")
    List<Category> findActiveCategoryTree();

    // FIX: New query to fetch the full tree for the admin view, including archived categories.
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findAdminCategoryTree();
}