package com.marketplace.seller.service;

import com.marketplace.common.exception.BusinessException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.seller.dto.VendorApplicationRequest;
import com.marketplace.seller.entity.VendorProfile;
import com.marketplace.seller.entity.VendorStatus;
import com.marketplace.seller.repository.VendorProfileRepository;
import com.marketplace.user.entity.Role;
import com.marketplace.user.entity.User;
import com.marketplace.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorProfileService {

    private final VendorProfileRepository vendorProfileRepository;
    private final UserRepository userRepository;

    public VendorProfileService(VendorProfileRepository vendorProfileRepository, UserRepository userRepository) {
        this.vendorProfileRepository = vendorProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VendorProfile apply(Long userId, VendorApplicationRequest request) {
        if (vendorProfileRepository.existsByUserId(userId)) {
            throw new BusinessException("Une demande vendeur existe deja pour ce compte");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        VendorProfile profile = new VendorProfile(user, request.companyName(), request.legalId(),
                request.contactPhone());
        return vendorProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public VendorProfile getOwnProfile(Long userId) {
        return vendorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun profil vendeur pour ce compte"));
    }

    @Transactional(readOnly = true)
    public Page<VendorProfile> list(VendorStatus status, Pageable pageable) {
        return status == null ? vendorProfileRepository.findAll(pageable)
                : vendorProfileRepository.findByStatus(status, pageable);
    }

    /**
     * Applique une decision administrative. L'approbation promeut le compte au role SELLER ;
     * la suspension et le rejet le ramenent au role CLIENT afin de couper immediatement
     * l'acces a l'espace vendeur.
     */
    @Transactional
    public VendorProfile decide(Long profileId, VendorStatus decision, String reason) {
        if (decision == VendorStatus.PENDING) {
            throw new BusinessException("Decision invalide");
        }
        VendorProfile profile = vendorProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil vendeur introuvable"));
        profile.decide(decision, reason);
        User user = profile.getUser();
        if (user.getRole() != Role.ADMIN) {
            user.setRole(decision == VendorStatus.APPROVED ? Role.SELLER : Role.CLIENT);
        }
        return profile;
    }
}
