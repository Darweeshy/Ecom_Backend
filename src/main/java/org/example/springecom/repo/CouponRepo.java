package org.example.springecom.repo;

import org.example.springecom.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon, Long> {
    // This custom method will allow us to find a coupon by its code
    Optional<Coupon> findByCode(String code);
}