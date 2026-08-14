package com.nexora.admin.controller;

import com.nexora.admin.dto.AdminWithdrawalDto;
import com.nexora.admin.dto.UpdateWithdrawalStatusRequest;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminWithdrawalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/withdrawals")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Retraits")
public class AdminWithdrawalController {

    private final AdminWithdrawalService adminWithdrawalService;

    @GetMapping
    public List<AdminWithdrawalDto> findAll() {
        return adminWithdrawalService.findAll();
    }

    @PatchMapping("/{id}/status")
    public AdminWithdrawalDto updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateWithdrawalStatusRequest request) {
        return adminWithdrawalService.updateStatus(id, request);
    }
}
