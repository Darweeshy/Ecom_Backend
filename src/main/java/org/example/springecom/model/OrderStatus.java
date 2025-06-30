package org.example.springecom.model;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_FAILED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}