package com.nexora.admin.dto;

import com.nexora.common.enums.ShopStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminShopDto {
    private Long id;
    private Long sellerId;
    private String sellerCompanyName;
    private String name;
    private String slug;
    private ShopStatus status;
    private boolean active;
    private LocalDateTime createdAt;
}
