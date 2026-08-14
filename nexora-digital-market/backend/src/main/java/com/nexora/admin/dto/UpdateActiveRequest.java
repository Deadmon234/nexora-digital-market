package com.nexora.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateActiveRequest {
    @NotNull
    private Boolean active;
}
