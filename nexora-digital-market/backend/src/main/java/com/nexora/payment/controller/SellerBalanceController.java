package com.nexora.payment.controller;

import com.nexora.payment.dto.CommissionDto;
import com.nexora.payment.dto.SellerBalanceDto;
import com.nexora.payment.dto.WithdrawalDto;
import com.nexora.payment.dto.WithdrawalRequestDto;
import com.nexora.payment.service.CommissionService;
import com.nexora.payment.service.SellerBalanceService;
import com.nexora.payment.service.WithdrawalService;
import com.nexora.seller.security.SellerAccess;
import com.nexora.seller.security.SellerContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/me")
@RequiredArgsConstructor
@SellerAccess
@PreAuthorize("hasAuthority('ROLE_SELLER')")
@Tag(name = "Revenus vendeur")
public class SellerBalanceController {

    private final SellerContextService sellerContextService;
    private final SellerBalanceService sellerBalanceService;
    private final CommissionService commissionService;
    private final WithdrawalService withdrawalService;

    @GetMapping("/balance")
    @Operation(summary = "Mon solde")
    public SellerBalanceDto getBalance() {
        return sellerBalanceService.getMyBalance(sellerContextService.requireApprovedSeller());
    }

    @GetMapping("/commissions")
    @Operation(summary = "Historique des commissions")
    public List<CommissionDto> getCommissions() {
        return commissionService.getMyCommissions(sellerContextService.requireApprovedSeller());
    }

    @GetMapping("/withdrawals")
    @Operation(summary = "Mes demandes de retrait")
    public List<WithdrawalDto> getWithdrawals() {
        return withdrawalService.getMyWithdrawals();
    }

    @PostMapping("/withdrawals")
    @Operation(summary = "Demander un retrait")
    public ResponseEntity<WithdrawalDto> requestWithdrawal(@Valid @RequestBody WithdrawalRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(withdrawalService.requestWithdrawal(request));
    }
}
