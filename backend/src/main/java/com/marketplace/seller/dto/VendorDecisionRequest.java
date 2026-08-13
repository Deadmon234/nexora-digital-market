package com.marketplace.seller.dto;

import jakarta.validation.constraints.Size;

public record VendorDecisionRequest(@Size(max = 500) String reason) {
}
