package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserOrderDTO {
    private Long id;
    private String orderTrackingNumber;
    private Date orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderItemDTO> orderItems; // We can reuse the OrderItemDTO

    public UserOrderDTO(Order order) {
        this.id = order.getId();
        this.orderTrackingNumber = order.getOrderTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.status = String.valueOf(order.getStatus());
        if (order.getOrderItems() != null) {
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        }
    }
}