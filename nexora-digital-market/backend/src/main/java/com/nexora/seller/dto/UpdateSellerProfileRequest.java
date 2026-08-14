package com.nexora.seller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSellerProfileRequest {
    private String companyName;
    private String taxId;
}
