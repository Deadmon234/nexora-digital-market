package com.nexora.admin.dto;

import com.nexora.common.enums.SellerStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminSellerDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String taxId;
    private SellerStatus status;
    private BigDecimal commissionRate;
    private LocalDateTime createdAt;
}
