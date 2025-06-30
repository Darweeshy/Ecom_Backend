package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrderAdminListDTO {
    private Long id;
    private String orderTrackingNumber;
    private String customerUsername;
    private Date orderDate;
    private BigDecimal totalPrice;
    private String status;

    public OrderAdminListDTO(Order order) {
        this.id = order.getId();
        this.orderTrackingNumber = order.getOrderTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.status = String.valueOf(order.getStatus());
        if (order.getUser() != null) {
            this.customerUsername = order.getUser().getUsername();
        }
    }
}