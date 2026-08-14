package com.nexora.admin.service;

import com.nexora.admin.dto.AdminAnalyticsDto;
import com.nexora.common.enums.OrderStatus;
import com.nexora.common.enums.SellerStatus;
import com.nexora.common.enums.WithdrawalStatus;
import com.nexora.order.repository.CustomerOrderRepository;
import com.nexora.payment.entity.Commission;
import com.nexora.payment.repository.CommissionRepository;
import com.nexora.payment.repository.WithdrawalRequestRepository;
import com.nexora.product.repository.ProductRepository;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository orderRepository;
    private final CommissionRepository commissionRepository;
    private final WithdrawalRequestRepository withdrawalRepository;

    @Transactional(readOnly = true)
    public AdminAnalyticsDto getDashboardStats() {
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED && o.getStatus() != OrderStatus.PENDING)
                .map(o -> o.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCommissions = commissionRepository.findAll().stream()
                .map(Commission::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AdminAnalyticsDto.builder()
                .totalUsers(userRepository.count())
                .totalSellers(sellerRepository.count())
                .pendingSellers(sellerRepository.countByStatus(SellerStatus.PENDING))
                .totalProducts(productRepository.count())
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByStatus(OrderStatus.PENDING))
                .totalRevenue(totalRevenue)
                .totalCommissions(totalCommissions)
                .pendingWithdrawals(withdrawalRepository.countByStatus(WithdrawalStatus.PENDING))
                .build();
    }
}
