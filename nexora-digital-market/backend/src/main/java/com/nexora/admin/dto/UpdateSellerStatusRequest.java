package com.nexora.admin.dto;

import com.nexora.common.enums.SellerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSellerStatusRequest {
    @NotNull
    private SellerStatus status;
}
