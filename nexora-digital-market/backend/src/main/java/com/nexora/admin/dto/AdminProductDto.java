package com.nexora.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminProductDto {
    private Long id;
    private String name;
    private String slug;
    private String categoryName;
    private String brandName;
    private int offerCount;
    private boolean active;
    private LocalDateTime createdAt;
}
