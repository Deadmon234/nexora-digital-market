package com.nexora.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {
    @NotNull
    private Long offerId;

    @Min(1)
    private int quantity = 1;
}
