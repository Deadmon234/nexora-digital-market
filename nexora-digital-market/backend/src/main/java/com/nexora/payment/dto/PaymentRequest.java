package com.nexora.payment.dto;

import com.nexora.common.enums.PaymentMethod;
import com.nexora.common.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod method;

    private String cardNumber;
}
