package com.nexora.inventory.dto;

import com.nexora.common.enums.InventoryMovementType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryMovementDto {
    private Long id;
    private Long offerId;
    private String productName;
    private InventoryMovementType type;
    private Integer quantity;
    private Integer stockBefore;
    private Integer stockAfter;
    private String reason;
    private LocalDateTime createdAt;
}
