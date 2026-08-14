package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private boolean active;
    private List<CategoryDto> children;
}
