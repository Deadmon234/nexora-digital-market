package com.marketplace.seller.dto;

import com.marketplace.seller.entity.VendorStatus;
import java.time.Instant;

public record VendorProfileResponse(Long id, Long userId, String email, String companyName, String legalId,
                                    String contactPhone, VendorStatus status, String decisionReason,
                                    Instant decidedAt) {
}
