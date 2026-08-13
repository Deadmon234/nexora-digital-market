package com.marketplace.seller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VendorApplicationRequest(
        @NotBlank @Size(max = 150) String companyName,
        @Size(max = 50) String legalId,
        @NotBlank @Size(max = 30) String contactPhone) {
}
