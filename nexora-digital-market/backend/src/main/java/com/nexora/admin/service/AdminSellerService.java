package com.nexora.admin.service;

import com.nexora.admin.dto.*;
import com.nexora.common.enums.SellerStatus;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSellerService {

    private final SellerRepository sellerRepository;

    @Transactional(readOnly = true)
    public List<AdminSellerDto> findAll() {
        return sellerRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminSellerDto findById(Long id) {
        return toDto(getSeller(id));
    }

    @Transactional
    public AdminSellerDto updateStatus(Long id, UpdateSellerStatusRequest request) {
        Seller seller = getSeller(id);
        if (request.getStatus() == SellerStatus.PENDING) {
            throw new ValidationException("Impossible de remettre un vendeur en attente");
        }
        seller.setStatus(request.getStatus());
        return toDto(sellerRepository.save(seller));
    }

    @Transactional
    public AdminSellerDto updateCommissionRate(Long id, UpdateCommissionRateRequest request) {
        Seller seller = getSeller(id);
        seller.setCommissionRate(request.getCommissionRate());
        return toDto(sellerRepository.save(seller));
    }

    private Seller getSeller(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendeur introuvable"));
    }

    private AdminSellerDto toDto(Seller seller) {
        User user = seller.getUser();
        return AdminSellerDto.builder()
                .id(seller.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(seller.getCompanyName())
                .taxId(seller.getTaxId())
                .status(seller.getStatus())
                .commissionRate(seller.getCommissionRate())
                .createdAt(seller.getCreatedAt())
                .build();
    }
}
