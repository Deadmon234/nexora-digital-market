package com.nexora.admin.dto;

import com.nexora.common.enums.WithdrawalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWithdrawalStatusRequest {
    @NotNull
    private WithdrawalStatus status;
}
