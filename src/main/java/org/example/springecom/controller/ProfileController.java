package org.example.springecom.controller;

import org.example.springecom.model.UserOrderDTO;
import org.example.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<UserOrderDTO>> getUserOrders(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<UserOrderDTO> orders = orderService.getOrdersForUser(userDetails.getUsername());
        return ResponseEntity.ok(orders);
    }
}