package com.nexora.seller.dto;

import com.nexora.common.enums.SellerStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SellerProfileDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String taxId;
    private SellerStatus status;
    private BigDecimal commissionRate;
}
