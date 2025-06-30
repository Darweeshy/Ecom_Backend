package org.example.springecom.service;

import org.example.springecom.model.Coupon;
import org.example.springecom.repo.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepo couponRepo;

    // --- Admin-facing methods ---

    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    public Coupon createCoupon(Coupon coupon) {
        // You could add validation here, e.g., to ensure discountValue is not negative
        return couponRepo.save(coupon);
    }

    public void deleteCoupon(Long id) {
        // This will trigger the @SQLDelete soft delete on the Coupon entity
        couponRepo.deleteById(id);
    }

    // --- Public-facing method ---

    public Optional<Coupon> validateCoupon(String code) {
        Optional<Coupon> couponOptional = couponRepo.findByCode(code);

        if (couponOptional.isEmpty()) {
            return Optional.empty(); // Coupon code does not exist
        }

        Coupon coupon = couponOptional.get();

        // Check if the coupon is expired
        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().before(new Date())) {
            return Optional.empty(); // Coupon is expired
        }

        // The coupon is valid
        return couponOptional;
    }
}