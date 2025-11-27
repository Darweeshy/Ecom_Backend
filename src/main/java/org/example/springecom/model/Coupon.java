// ========== Coupon.java ==========
package org.example.springecom.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    // FIX: Field renamed from 'active' to 'archived'
    private boolean archived = false;
}

