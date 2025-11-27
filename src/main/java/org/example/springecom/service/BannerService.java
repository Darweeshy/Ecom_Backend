package org.example.springecom.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.springecom.model.Banner;
import org.example.springecom.repo.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerRepo bannerRepo;

    @Transactional(readOnly = true)
    public List<Banner> getAllBanners() {
        return bannerRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Banner> getActiveBanners() {
        return bannerRepo.findActiveBannersForDisplay(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Banner getBannerById(Long id) {
        return bannerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found with id: " + id));
    }

    @Transactional
    public Banner createBanner(Banner banner) {
        return bannerRepo.save(banner);
    }

    @Transactional
    public Banner updateBanner(Long id, Banner bannerDetails) {
        Banner banner = getBannerById(id);

        banner.setTitle(bannerDetails.getTitle());
        banner.setSubtitle(bannerDetails.getSubtitle());
        banner.setImageUrl(bannerDetails.getImageUrl());
        banner.setLinkUrl(bannerDetails.getLinkUrl());
        banner.setCtaText(bannerDetails.getCtaText());
        banner.setDisplayOrder(bannerDetails.getDisplayOrder());
        banner.setIsActive(bannerDetails.getIsActive());
        banner.setStartDate(bannerDetails.getStartDate());
        banner.setEndDate(bannerDetails.getEndDate());
        banner.setBackgroundColor(bannerDetails.getBackgroundColor());
        banner.setTextColor(bannerDetails.getTextColor());

        return bannerRepo.save(banner);
    }

    @Transactional
    public void deleteBanner(Long id) {
        Banner banner = getBannerById(id);
        bannerRepo.delete(banner);
    }
}
