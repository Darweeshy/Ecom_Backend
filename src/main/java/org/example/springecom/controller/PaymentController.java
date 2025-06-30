package org.example.springecom.controller;

import org.example.springecom.model.Order;
import org.example.springecom.service.OrderService;
import org.example.springecom.service.PaymobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymobService paymobService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/paymob/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestBody Map<String, Long> payload) {
        Long orderId = payload.get("orderId");
        Order order = orderService.getOrderById(orderId);
        String paymentKey = paymobService.initiatePayment(order);

        // Save the order again to store the new paymobOrderId
        orderService.saveOrder(order);

        return ResponseEntity.ok(Map.of("payment_key", paymentKey));
    }
}