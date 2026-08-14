package com.nexora.payment.service;

import com.nexora.order.entity.CustomerOrder;
import com.nexora.order.entity.SellerOrder;
import com.nexora.payment.entity.Commission;
import com.nexora.payment.entity.Payment;
import com.nexora.payment.repository.CommissionRepository;
import com.nexora.payment.dto.CommissionDto;
import com.nexora.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommissionService {

    private final CommissionRepository commissionRepository;
    private final SellerBalanceService sellerBalanceService;

    @Transactional(readOnly = true)
    public List<CommissionDto> getMyCommissions(Seller seller) {
        return commissionRepository.findBySellerOrderByCreatedAtDesc(seller).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void processCommissions(Payment payment, CustomerOrder order) {
        for (SellerOrder sellerOrder : order.getSellerOrders()) {
            Seller seller = sellerOrder.getSeller();
            BigDecimal orderAmount = sellerOrder.getSubtotal();
            BigDecimal rate = seller.getCommissionRate() != null
                    ? seller.getCommissionRate()
                    : new BigDecimal("10.00");

            BigDecimal commissionAmount = orderAmount
                    .multiply(rate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal sellerAmount = orderAmount.subtract(commissionAmount);

            commissionRepository.save(Commission.builder()
                    .sellerOrder(sellerOrder)
                    .seller(seller)
                    .payment(payment)
                    .orderAmount(orderAmount)
                    .commissionRate(rate)
                    .commissionAmount(commissionAmount)
                    .sellerAmount(sellerAmount)
                    .build());

            sellerBalanceService.credit(seller, sellerAmount);
        }
    }

    private CommissionDto toDto(Commission commission) {
        return CommissionDto.builder()
                .id(commission.getId())
                .sellerOrderId(commission.getSellerOrder().getId())
                .orderNumber(commission.getSellerOrder().getOrder().getOrderNumber())
                .orderAmount(commission.getOrderAmount())
                .commissionRate(commission.getCommissionRate())
                .commissionAmount(commission.getCommissionAmount())
                .sellerAmount(commission.getSellerAmount())
                .createdAt(commission.getCreatedAt())
                .build();
    }
}
