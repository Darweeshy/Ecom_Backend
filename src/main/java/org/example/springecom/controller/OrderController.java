package org.example.springecom.controller;

import org.example.springecom.model.Order;
import org.example.springecom.model.PublicOrderTrackingDTO; // <-- IMPORT THE NEW DTO
import org.example.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> createOrder(@RequestBody Order orderRequest) {
        Order newOrder = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<PublicOrderTrackingDTO> getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        // This method now returns the safe DTO, which prevents the crash.
        return orderService.getOrderByTrackingNumber(trackingNumber)
                .map(order -> new PublicOrderTrackingDTO(order))
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}