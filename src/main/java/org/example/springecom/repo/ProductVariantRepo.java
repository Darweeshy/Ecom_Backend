package org.example.springecom.repo;

import org.example.springecom.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariantRepo extends JpaRepository<ProductVariant, Long> {
    @Query("SELECT DISTINCT pv.size FROM ProductVariant pv ORDER BY pv.size")
    List<String> findDistinctSizes();
}