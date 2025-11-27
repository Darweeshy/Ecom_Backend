package org.example.springecom.controller;

import org.example.springecom.model.Banner;
import org.example.springecom.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicBannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/banners")
    public ResponseEntity<List<Banner>> getActiveBanners() {
        return ResponseEntity.ok(bannerService.getActiveBanners());
    }
}
