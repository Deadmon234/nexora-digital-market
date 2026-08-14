package com.nexora.shop.dto;

import com.nexora.common.enums.ShopStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private ShopStatus status;
    private boolean active;
}
