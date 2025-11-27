package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PublicOrderTrackingDTO {
    private String orderTrackingNumber;
    private Date orderDate;
    private String status;

    public PublicOrderTrackingDTO(Order order) {
        this.orderTrackingNumber = order.getOrderTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.status = String.valueOf(order.getStatus());
    }
}