package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private boolean active;
}
