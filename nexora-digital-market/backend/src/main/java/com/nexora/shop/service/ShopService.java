package com.nexora.shop.service;

import com.nexora.common.enums.ShopStatus;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.common.util.SlugUtils;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.security.SellerContextService;
import com.nexora.shop.dto.ShopDto;
import com.nexora.shop.dto.ShopRequest;
import com.nexora.shop.entity.Shop;
import com.nexora.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final SellerContextService sellerContextService;

    @Transactional(readOnly = true)
    public ShopDto getPublicShop(String slug) {
        Shop shop = shopRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable : " + slug));
        return toDto(shop);
    }

    @Transactional(readOnly = true)
    public ShopDto getMyShop() {
        Seller seller = sellerContextService.getCurrentSeller();
        Shop shop = shopRepository.findBySeller(seller).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable"));
        return toDto(shop);
    }

    @Transactional
    public ShopDto createOrUpdateShop(ShopRequest request) {
        Seller seller = sellerContextService.getCurrentSeller();
        List<Shop> existing = shopRepository.findBySeller(seller);

        Shop shop;
        if (existing.isEmpty()) {
            String slug = generateUniqueSlug(request.getName());
            shop = Shop.builder()
                    .seller(seller)
                    .name(request.getName())
                    .slug(slug)
                    .description(request.getDescription())
                    .logoUrl(request.getLogoUrl())
                    .bannerUrl(request.getBannerUrl())
                    .status(ShopStatus.PENDING)
                    .active(true)
                    .build();
        } else {
            shop = existing.getFirst();
            shop.setName(request.getName());
            shop.setDescription(request.getDescription());
            shop.setLogoUrl(request.getLogoUrl());
            shop.setBannerUrl(request.getBannerUrl());
        }

        return toDto(shopRepository.save(shop));
    }

    private String generateUniqueSlug(String name) {
        String base = SlugUtils.toSlug(name);
        String slug = base;
        int counter = 1;
        while (shopRepository.existsBySlug(slug)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private ShopDto toDto(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .slug(shop.getSlug())
                .description(shop.getDescription())
                .logoUrl(shop.getLogoUrl())
                .bannerUrl(shop.getBannerUrl())
                .status(shop.getStatus())
                .active(shop.isActive())
                .build();
    }
}
