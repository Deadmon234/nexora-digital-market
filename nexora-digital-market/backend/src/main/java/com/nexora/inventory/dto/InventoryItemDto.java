package com.nexora.inventory.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemDto {
    private Long offerId;
    private Long productId;
    private String productName;
    private Integer stock;
    private boolean lowStock;
}
