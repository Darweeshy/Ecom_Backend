package org.example.springecom.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderTrackingNumber;
    private String location;
    private String countryCode;
    private String phoneNumber;

    // FIX: Changed status from String to the OrderStatus Enum for type safety.
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long paymobOrderId;
    private String shipmentTrackingNumber;

    private BigDecimal totalPrice;
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @PrePersist
    protected void onCreate() {
        this.orderTrackingNumber = UUID.randomUUID().toString();
        this.orderDate = new Date();
        // Set the initial status using the Enum
        this.status = OrderStatus.PENDING_PAYMENT;
    }
}