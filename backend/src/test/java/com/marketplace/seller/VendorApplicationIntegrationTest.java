package com.marketplace.seller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.marketplace.AbstractIntegrationTest;
import com.marketplace.common.exception.BusinessException;
import com.marketplace.seller.dto.VendorApplicationRequest;
import com.marketplace.seller.entity.VendorProfile;
import com.marketplace.seller.entity.VendorStatus;
import com.marketplace.seller.service.VendorProfileService;
import com.marketplace.user.entity.Role;
import com.marketplace.user.entity.User;
import com.marketplace.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VendorApplicationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private VendorProfileService vendorProfileService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void approvalPromotesAccountToSeller() {
        User user = newClient();
        VendorProfile profile = vendorProfileService.apply(user.getId(),
                new VendorApplicationRequest("Tech Store Douala", "RC123", "+237600000000"));
        assertThat(profile.getStatus()).isEqualTo(VendorStatus.PENDING);

        vendorProfileService.decide(profile.getId(), VendorStatus.APPROVED, null);

        assertThat(userRepository.findById(user.getId()).orElseThrow().getRole()).isEqualTo(Role.SELLER);
    }

    @Test
    void suspensionRevokesSellerRole() {
        User user = newClient();
        VendorProfile profile = vendorProfileService.apply(user.getId(),
                new VendorApplicationRequest("Tech Store Yaounde", null, "+237600000001"));
        vendorProfileService.decide(profile.getId(), VendorStatus.APPROVED, null);

        vendorProfileService.decide(profile.getId(), VendorStatus.SUSPENDED, "Documents invalides");

        assertThat(userRepository.findById(user.getId()).orElseThrow().getRole()).isEqualTo(Role.CLIENT);
    }

    @Test
    void applicationIsUniquePerAccount() {
        User user = newClient();
        VendorApplicationRequest request = new VendorApplicationRequest("Shop", null, "+237600000002");
        vendorProfileService.apply(user.getId(), request);

        assertThatThrownBy(() -> vendorProfileService.apply(user.getId(), request))
                .isInstanceOf(BusinessException.class);
    }

    private User newClient() {
        return userRepository.save(new User("client-" + System.nanoTime() + "@nexora.test", "hash", Role.CLIENT,
                "Test", "User", null));
    }
}
