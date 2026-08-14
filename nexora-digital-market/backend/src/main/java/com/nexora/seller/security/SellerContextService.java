package com.nexora.seller.security;

import com.nexora.auth.security.UserPrincipal;
import com.nexora.common.enums.RoleName;
import com.nexora.common.enums.SellerStatus;
import com.nexora.common.exception.NexoraAuthenticationException;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.product.entity.ProductOffer;
import com.nexora.product.repository.ProductOfferRepository;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.user.entity.User;
import com.nexora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerContextService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ProductOfferRepository productOfferRepository;

    @Transactional(readOnly = true)
    public UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new NexoraAuthenticationException("Non authentifié");
        }
        return principal;
    }

    @Transactional(readOnly = true)
    public Seller getCurrentSeller() {
        UserPrincipal principal = getCurrentUser();
        if (!principal.hasRole(RoleName.ROLE_SELLER)) {
            throw new ValidationException("Accès réservé aux vendeurs");
        }
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        return sellerRepository.findByUser(user)
                .orElseThrow(() -> new ValidationException("Profil vendeur non trouvé. Postulez d'abord."));
    }

    @Transactional(readOnly = true)
    public Seller requireApprovedSeller() {
        Seller seller = getCurrentSeller();
        if (seller.getStatus() != SellerStatus.APPROVED) {
            throw new ValidationException("Compte vendeur en attente d'approbation");
        }
        return seller;
    }

    @Transactional(readOnly = true)
    public ProductOffer requireOwnedOffer(Long offerId) {
        Seller seller = requireApprovedSeller();
        return productOfferRepository.findByIdAndSeller(offerId, seller)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable ou accès refusé"));
    }
}
