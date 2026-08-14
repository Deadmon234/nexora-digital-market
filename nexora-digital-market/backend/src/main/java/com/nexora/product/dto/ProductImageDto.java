package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageDto {

    private Long id;
    private String url;
    private String altText;
    private int displayOrder;
    private boolean primary;
}
