package com.nexora.admin.dto;

import com.nexora.common.enums.ShopStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateShopStatusRequest {
    @NotNull
    private ShopStatus status;
}
