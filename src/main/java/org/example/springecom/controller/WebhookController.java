package org.example.springecom.controller;

import org.example.springecom.model.Order;
import org.example.springecom.service.EmailService;
import org.example.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/paymob")
    public ResponseEntity<Void> handlePaymobWebhook(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> obj = (Map<String, Object>) payload.get("obj");
            boolean success = (boolean) obj.get("success");
            long orderIdFromPaymob = Long.parseLong((String) obj.get("merchant_order_id"));

            Order order = orderService.getOrderById(orderIdFromPaymob);

            if (success && !"PROCESSING".equals(order.getStatus())) {
                orderService.updateOrderStatus(orderIdFromPaymob, "PROCESSING");
                // Send confirmation email only after payment is confirmed and status is updated
                emailService.sendOrderConfirmationEmail(order);
            } else if (!success && !"PAYMENT_FAILED".equals(order.getStatus())) {
                orderService.updateOrderStatus(orderIdFromPaymob, "PAYMENT_FAILED");
                // Optional: Send a payment failure email here
            }
        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}