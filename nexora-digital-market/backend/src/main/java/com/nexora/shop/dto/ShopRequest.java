package com.nexora.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopRequest {
    @NotBlank
    private String name;
    private String description;
    private String logoUrl;
    private String bannerUrl;
}
