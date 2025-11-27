package org.example.springecom.controller;

import org.example.springecom.model.OrderAdminDetailsDTO;
import org.example.springecom.model.OrderAdminListDTO;
import org.example.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderAdminListDTO>> getAllOrders() {
        List<OrderAdminListDTO> orders = orderService.getAllOrdersForAdmin();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderAdminDetailsDTO> getOrderById(@PathVariable Long orderId) {
        OrderAdminDetailsDTO order = orderService.getOrderDetailsForAdmin(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderAdminDetailsDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        OrderAdminDetailsDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}