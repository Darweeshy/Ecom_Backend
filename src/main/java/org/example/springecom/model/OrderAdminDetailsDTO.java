package org.example.springecom.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderAdminDetailsDTO {
    private Long id;
    private String orderTrackingNumber;
    private String customerUsername;
    private String customerEmail;
    private String shippingAddress;
    private String phoneNumber;
    private Date orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderItemDTO> orderItems;

    public OrderAdminDetailsDTO(Order order) {
        this.id = order.getId();
        this.orderTrackingNumber = order.getOrderTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.status = String.valueOf(order.getStatus());
        this.shippingAddress = order.getLocation();
        this.phoneNumber = order.getCountryCode() + " " + order.getPhoneNumber();

        if (order.getUser() != null) {
            this.customerUsername = order.getUser().getUsername();
            this.customerEmail = order.getUser().getEmail();
        }

        if (order.getOrderItems() != null) {
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        }
    }
}