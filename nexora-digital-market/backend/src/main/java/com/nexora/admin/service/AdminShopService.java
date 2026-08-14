package com.nexora.admin.service;

import com.nexora.admin.dto.AdminShopDto;
import com.nexora.admin.dto.UpdateActiveRequest;
import com.nexora.admin.dto.UpdateShopStatusRequest;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.common.enums.ShopStatus;
import com.nexora.shop.entity.Shop;
import com.nexora.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminShopService {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public List<AdminShopDto> findAll() {
        return shopRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminShopDto updateStatus(Long id, UpdateShopStatusRequest request) {
        Shop shop = getShop(id);
        if (request.getStatus() == ShopStatus.PENDING) {
            throw new ValidationException("Impossible de remettre une boutique en attente");
        }
        shop.setStatus(request.getStatus());
        return toDto(shopRepository.save(shop));
    }

    @Transactional
    public AdminShopDto updateActive(Long id, UpdateActiveRequest request) {
        Shop shop = getShop(id);
        shop.setActive(request.getActive());
        return toDto(shopRepository.save(shop));
    }

    private Shop getShop(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable"));
    }

    private AdminShopDto toDto(Shop shop) {
        return AdminShopDto.builder()
                .id(shop.getId())
                .sellerId(shop.getSeller().getId())
                .sellerCompanyName(shop.getSeller().getCompanyName())
                .name(shop.getName())
                .slug(shop.getSlug())
                .status(shop.getStatus())
                .active(shop.isActive())
                .createdAt(shop.getCreatedAt())
                .build();
    }
}
