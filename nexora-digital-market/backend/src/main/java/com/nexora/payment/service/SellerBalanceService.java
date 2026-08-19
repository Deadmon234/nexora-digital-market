package com.nexora.payment.service;

import com.nexora.common.exception.ValidationException;
import com.nexora.payment.dto.SellerBalanceDto;
import com.nexora.payment.entity.SellerBalance;
import com.nexora.payment.repository.SellerBalanceRepository;
import com.nexora.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SellerBalanceService {

    private final SellerBalanceRepository sellerBalanceRepository;

    @Transactional
    public SellerBalanceDto getMyBalance(Seller seller) {
        return toDto(getOrCreate(seller));
    }

    @Transactional
    public void credit(Seller seller, BigDecimal amount) {
        SellerBalance balance = getOrCreate(seller);
        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
        balance.setTotalEarned(balance.getTotalEarned().add(amount));
        sellerBalanceRepository.save(balance);
    }

    @Transactional
    public void debitForWithdrawal(Seller seller, BigDecimal amount) {
        SellerBalance balance = getOrCreate(seller);
        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new ValidationException("Solde insuffisant");
        }
        balance.setAvailableBalance(balance.getAvailableBalance().subtract(amount));
        balance.setTotalWithdrawn(balance.getTotalWithdrawn().add(amount));
        sellerBalanceRepository.save(balance);
    }

    @Transactional
    public void refundWithdrawal(Seller seller, BigDecimal amount) {
        SellerBalance balance = getOrCreate(seller);
        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
        balance.setTotalWithdrawn(balance.getTotalWithdrawn().subtract(amount));
        sellerBalanceRepository.save(balance);
    }

    private SellerBalance getOrCreate(Seller seller) {
        return sellerBalanceRepository.findBySeller(seller).orElseGet(() ->
                sellerBalanceRepository.save(SellerBalance.builder().seller(seller).build()));
    }

    private SellerBalanceDto toDto(SellerBalance balance) {
        return SellerBalanceDto.builder()
                .availableBalance(balance.getAvailableBalance())
                .totalEarned(balance.getTotalEarned())
                .totalWithdrawn(balance.getTotalWithdrawn())
                .build();
    }
}
