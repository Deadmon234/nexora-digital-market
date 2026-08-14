package com.nexora.inventory.service;

import com.nexora.common.dto.PageResponse;
import com.nexora.common.enums.InventoryMovementType;
import com.nexora.common.exception.ValidationException;
import com.nexora.inventory.dto.InventoryItemDto;
import com.nexora.inventory.dto.InventoryMovementDto;
import com.nexora.inventory.entity.InventoryMovement;
import com.nexora.inventory.repository.InventoryMovementRepository;
import com.nexora.product.entity.ProductOffer;
import com.nexora.product.repository.ProductOfferRepository;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.security.SellerContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final SellerContextService sellerContextService;
    private final ProductOfferRepository productOfferRepository;
    private final InventoryMovementRepository inventoryMovementRepository;

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getInventory() {
        Seller seller = sellerContextService.requireApprovedSeller();
        return productOfferRepository.findBySellerOrderByCreatedAtDesc(seller).stream()
                .filter(ProductOffer::isActive)
                .map(offer -> InventoryItemDto.builder()
                        .offerId(offer.getId())
                        .productId(offer.getProduct().getId())
                        .productName(offer.getProduct().getName())
                        .stock(offer.getStock())
                        .lowStock(offer.getStock() != null && offer.getStock() <= LOW_STOCK_THRESHOLD)
                        .build())
                .toList();
    }

    @Transactional
    public InventoryItemDto addStock(Long offerId, Integer quantity, String reason) {
        return adjustStock(offerId, quantity, InventoryMovementType.ADD, reason);
    }

    @Transactional
    public InventoryItemDto decreaseStock(Long offerId, Integer quantity, String reason) {
        return adjustStock(offerId, quantity, InventoryMovementType.DECREASE, reason);
    }

    @Transactional
    public void decreaseStockForOrder(ProductOffer offer, int quantity, String reason) {
        if (quantity <= 0) {
            throw new ValidationException("La quantité doit être positive");
        }
        int before = offer.getStock() != null ? offer.getStock() : 0;
        if (before < quantity) {
            throw new ValidationException("Stock insuffisant pour " + offer.getProduct().getName());
        }
        int after = before - quantity;
        offer.setStock(after);
        productOfferRepository.save(offer);

        inventoryMovementRepository.save(InventoryMovement.builder()
                .seller(offer.getSeller())
                .productOffer(offer)
                .type(InventoryMovementType.DECREASE)
                .quantity(quantity)
                .stockBefore(before)
                .stockAfter(after)
                .reason(reason)
                .build());
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryMovementDto> getHistory(int page, int size) {
        Seller seller = sellerContextService.requireApprovedSeller();
        var result = inventoryMovementRepository
                .findBySellerOrderByCreatedAtDesc(seller, PageRequest.of(page, size))
                .map(this::toMovementDto);
        return PageResponse.from(result);
    }

    private InventoryItemDto adjustStock(Long offerId, Integer quantity, InventoryMovementType type, String reason) {
        if (quantity == null || quantity <= 0) {
            throw new ValidationException("La quantité doit être positive");
        }

        ProductOffer offer = sellerContextService.requireOwnedOffer(offerId);
        int before = offer.getStock() != null ? offer.getStock() : 0;
        int after = type == InventoryMovementType.ADD ? before + quantity : before - quantity;

        if (after < 0) {
            throw new ValidationException("Stock insuffisant");
        }

        offer.setStock(after);
        productOfferRepository.save(offer);

        inventoryMovementRepository.save(InventoryMovement.builder()
                .seller(offer.getSeller())
                .productOffer(offer)
                .type(type)
                .quantity(quantity)
                .stockBefore(before)
                .stockAfter(after)
                .reason(reason)
                .build());

        return InventoryItemDto.builder()
                .offerId(offer.getId())
                .productId(offer.getProduct().getId())
                .productName(offer.getProduct().getName())
                .stock(after)
                .lowStock(after <= LOW_STOCK_THRESHOLD)
                .build();
    }

    private InventoryMovementDto toMovementDto(InventoryMovement movement) {
        return InventoryMovementDto.builder()
                .id(movement.getId())
                .offerId(movement.getProductOffer().getId())
                .productName(movement.getProductOffer().getProduct().getName())
                .type(movement.getType())
                .quantity(movement.getQuantity())
                .stockBefore(movement.getStockBefore())
                .stockAfter(movement.getStockAfter())
                .reason(movement.getReason())
                .createdAt(movement.getCreatedAt())
                .build();
    }
}
