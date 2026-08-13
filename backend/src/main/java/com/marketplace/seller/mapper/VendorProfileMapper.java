package com.marketplace.seller.mapper;

import com.marketplace.seller.dto.VendorProfileResponse;
import com.marketplace.seller.entity.VendorProfile;
import org.springframework.stereotype.Component;

@Component
public class VendorProfileMapper {

    public VendorProfileResponse toResponse(VendorProfile profile) {
        return new VendorProfileResponse(profile.getId(), profile.getUser().getId(), profile.getUser().getEmail(),
                profile.getCompanyName(), profile.getLegalId(), profile.getContactPhone(), profile.getStatus(),
                profile.getDecisionReason(), profile.getDecidedAt());
    }
}
