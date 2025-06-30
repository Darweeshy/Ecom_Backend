package org.example.springecom.repo;

import org.example.springecom.model.OrderItem;
import org.example.springecom.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    // This method finds all order items that contain any of the specified product variants.
    List<OrderItem> findByVariantIn(List<ProductVariant> variants);
}