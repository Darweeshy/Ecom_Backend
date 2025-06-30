package org.example.springecom.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.springecom.exception.InsufficientStockException;
import org.example.springecom.model.*;
import org.example.springecom.repo.OrderRepo;
import org.example.springecom.repo.ProductVariantRepo;
import org.example.springecom.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired private OrderRepo orderRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private ProductVariantRepo variantRepo;

    // FIX: Added @Transactional to ensure stock update and order creation are atomic.
    @Transactional
    public Order createOrder(Order orderRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByUsername(userDetails.getUsername());

        Order order = new Order();
        order.setUser(user);
        order.setLocation(orderRequest.getLocation());
        order.setCountryCode(orderRequest.getCountryCode());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setTotalPrice(orderRequest.getTotalPrice());

        for (OrderItem itemRequest : orderRequest.getOrderItems()) {
            ProductVariant variant = variantRepo.findById(itemRequest.getVariant().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

            if (variant.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + variant.getProduct().getName());
            }

            variant.setStockQuantity(variant.getStockQuantity() - itemRequest.getQuantity());
            variantRepo.save(variant);

            itemRequest.setOrder(order);
        }

        order.setOrderItems(orderRequest.getOrderItems());
        return orderRepo.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderByTrackingNumber(String trackingNumber) {
        return orderRepo.findByOrderTrackingNumber(trackingNumber);
    }

    @Transactional(readOnly = true)
    public List<OrderAdminListDTO> getAllOrdersForAdmin() {
        return orderRepo.findAll().stream().map(OrderAdminListDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderAdminDetailsDTO getOrderDetailsForAdmin(Long orderId) {
        return orderRepo.findOrderWithDetailsById(orderId)
                .map(OrderAdminDetailsDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    // FIX: Added @Transactional for atomicity.
    @Transactional
    public OrderAdminDetailsDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(OrderStatus.valueOf(newStatus));
        Order updatedOrder = orderRepo.save(order);
        return new OrderAdminDetailsDTO(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<UserOrderDTO> getOrdersForUser(String username) {
        return orderRepo.findUserOrdersWithDetails(username).stream()
                .map(UserOrderDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
}