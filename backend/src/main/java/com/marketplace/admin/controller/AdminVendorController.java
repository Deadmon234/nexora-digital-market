package com.marketplace.admin.controller;

import com.marketplace.seller.dto.VendorDecisionRequest;
import com.marketplace.seller.dto.VendorProfileResponse;
import com.marketplace.seller.entity.VendorStatus;
import com.marketplace.seller.mapper.VendorProfileMapper;
import com.marketplace.seller.service.VendorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/vendors")
@Tag(name = "Administration des vendeurs")
public class AdminVendorController {

    private final VendorProfileService vendorProfileService;
    private final VendorProfileMapper vendorProfileMapper;

    public AdminVendorController(VendorProfileService vendorProfileService, VendorProfileMapper vendorProfileMapper) {
        this.vendorProfileService = vendorProfileService;
        this.vendorProfileMapper = vendorProfileMapper;
    }

    @GetMapping
    @Operation(summary = "Liste des profils vendeurs, filtrable par statut")
    public Page<VendorProfileResponse> list(@RequestParam(required = false) VendorStatus status, Pageable pageable) {
        return vendorProfileService.list(status, pageable).map(vendorProfileMapper::toResponse);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approuve une demande vendeur et promeut le compte au role SELLER")
    public VendorProfileResponse approve(@PathVariable Long id, @Valid @RequestBody VendorDecisionRequest request) {
        return vendorProfileMapper.toResponse(
                vendorProfileService.decide(id, VendorStatus.APPROVED, request.reason()));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rejette une demande vendeur")
    public VendorProfileResponse reject(@PathVariable Long id, @Valid @RequestBody VendorDecisionRequest request) {
        return vendorProfileMapper.toResponse(
                vendorProfileService.decide(id, VendorStatus.REJECTED, request.reason()));
    }

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspend un vendeur et lui retire l'acces a l'espace vendeur")
    public VendorProfileResponse suspend(@PathVariable Long id, @Valid @RequestBody VendorDecisionRequest request) {
        return vendorProfileMapper.toResponse(
                vendorProfileService.decide(id, VendorStatus.SUSPENDED, request.reason()));
    }
}
