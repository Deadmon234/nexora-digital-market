package com.marketplace.seller.controller;

import com.marketplace.security.AuthenticatedUser;
import com.marketplace.seller.dto.VendorApplicationRequest;
import com.marketplace.seller.dto.VendorProfileResponse;
import com.marketplace.seller.mapper.VendorProfileMapper;
import com.marketplace.seller.service.VendorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Vendeur")
public class SellerController {

    private final VendorProfileService vendorProfileService;
    private final VendorProfileMapper vendorProfileMapper;

    public SellerController(VendorProfileService vendorProfileService, VendorProfileMapper vendorProfileMapper) {
        this.vendorProfileService = vendorProfileService;
        this.vendorProfileMapper = vendorProfileMapper;
    }

    @PostMapping("/vendor-applications")
    @Operation(summary = "Demande pour devenir vendeur, soumise par un client authentifie")
    public ResponseEntity<VendorProfileResponse> apply(@AuthenticationPrincipal AuthenticatedUser principal,
                                                       @Valid @RequestBody VendorApplicationRequest request) {
        VendorProfileResponse body = vendorProfileMapper
                .toResponse(vendorProfileService.apply(principal.getId(), request));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/seller/profile")
    @Operation(summary = "Profil vendeur du compte authentifie")
    public VendorProfileResponse profile(@AuthenticationPrincipal AuthenticatedUser principal) {
        return vendorProfileMapper.toResponse(vendorProfileService.getOwnProfile(principal.getId()));
    }
}
