package com.nexora.seller.service;

import com.nexora.common.enums.RoleName;
import com.nexora.common.enums.SellerStatus;
import com.nexora.common.exception.ValidationException;
import com.nexora.seller.dto.SellerApplyRequest;
import com.nexora.seller.dto.SellerDashboardDto;
import com.nexora.seller.dto.SellerProfileDto;
import com.nexora.seller.dto.UpdateSellerProfileRequest;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.seller.security.SellerContextService;
import com.nexora.shop.entity.Shop;
import com.nexora.shop.repository.ShopRepository;
import com.nexora.user.entity.Role;
import com.nexora.user.entity.User;
import com.nexora.user.repository.RoleRepository;
import com.nexora.user.repository.UserRepository;
import com.nexora.product.repository.ProductOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final SellerRepository sellerRepository;
    private final SellerContextService sellerContextService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShopRepository shopRepository;
    private final ProductOfferRepository productOfferRepository;

    @Transactional(readOnly = true)
    public SellerProfileDto getMyProfile() {
        Seller seller = sellerContextService.getCurrentSeller();
        User user = seller.getUser();
        return SellerProfileDto.builder()
                .id(seller.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(seller.getCompanyName())
                .taxId(seller.getTaxId())
                .status(seller.getStatus())
                .commissionRate(seller.getCommissionRate())
                .build();
    }

    @Transactional
    public SellerProfileDto updateMyProfile(UpdateSellerProfileRequest request) {
        Seller seller = sellerContextService.getCurrentSeller();
        if (request.getCompanyName() != null) {
            seller.setCompanyName(request.getCompanyName());
        }
        if (request.getTaxId() != null) {
            seller.setTaxId(request.getTaxId());
        }
        sellerRepository.save(seller);
        return getMyProfile();
    }

    @Transactional
    public SellerProfileDto applyAsSeller(SellerApplyRequest request) {
        var principal = sellerContextService.getCurrentUser();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ValidationException("Utilisateur introuvable"));

        if (sellerRepository.existsByUser(user)) {
            throw new ValidationException("Vous êtes déjà vendeur");
        }

        Role sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER)
                .orElseThrow(() -> new ValidationException("Rôle vendeur indisponible"));
        user.getRoles().add(sellerRole);
        userRepository.save(user);

        Seller seller = Seller.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .taxId(request.getTaxId())
                .status(SellerStatus.PENDING)
                .build();
        sellerRepository.save(seller);

        return SellerProfileDto.builder()
                .id(seller.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(seller.getCompanyName())
                .taxId(seller.getTaxId())
                .status(seller.getStatus())
                .commissionRate(seller.getCommissionRate())
                .build();
    }

    @Transactional(readOnly = true)
    public SellerDashboardDto getDashboard() {
        Seller seller = sellerContextService.getCurrentSeller();
        var offers = productOfferRepository.findBySellerOrderByCreatedAtDesc(seller);

        long totalStock = offers.stream()
                .filter(o -> o.isActive())
                .mapToLong(o -> o.getStock() != null ? o.getStock() : 0)
                .sum();
        long lowStock = offers.stream()
                .filter(o -> o.isActive() && o.getStock() != null && o.getStock() <= LOW_STOCK_THRESHOLD)
                .count();

        String shopName = shopRepository.findBySeller(seller).stream()
                .findFirst()
                .map(Shop::getName)
                .orElse(null);

        return SellerDashboardDto.builder()
                .totalProducts(offers.stream().filter(o -> o.isActive()).count())
                .totalStock(totalStock)
                .lowStockProducts(lowStock)
                .shopName(shopName)
                .sellerStatus(seller.getStatus().name())
                .build();
    }
}
