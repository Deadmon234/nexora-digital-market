package com.nexora.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryAdminRequest {
    @NotBlank
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private Boolean active;
}
