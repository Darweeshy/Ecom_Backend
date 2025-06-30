package org.example.springecom.controller;

import org.example.springecom.model.Coupon;
import org.example.springecom.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class PublicCouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/validate")
    public ResponseEntity<Coupon> validateCoupon(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        return couponService.validateCoupon(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}