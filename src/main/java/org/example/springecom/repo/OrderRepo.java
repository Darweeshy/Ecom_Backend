// src/main/java/org/example/springecom/repo/OrderRepo.java
package org.example.springecom.repo;

import org.example.springecom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderTrackingNumber(String orderTrackingNumber);
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.variant v JOIN FETCH v.product WHERE o.id = :orderId")
    Optional<Order> findOrderWithDetailsById(@Param("orderId") Long orderId);
    // --- END OF CHANGE ---
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.variant v JOIN FETCH v.product WHERE o.user.username = :username ORDER BY o.orderDate DESC")
    List<Order> findUserOrdersWithDetails(@Param("username") String username);

}