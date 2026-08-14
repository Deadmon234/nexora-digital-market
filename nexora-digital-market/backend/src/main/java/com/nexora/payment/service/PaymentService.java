package com.nexora.payment.service;

import com.nexora.common.enums.OrderStatus;
import com.nexora.common.enums.PaymentStatus;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.order.entity.CustomerOrder;
import com.nexora.order.repository.CustomerOrderRepository;
import com.nexora.payment.dto.PaymentDto;
import com.nexora.payment.dto.PaymentRequest;
import com.nexora.payment.entity.Payment;
import com.nexora.payment.repository.PaymentRepository;
import com.nexora.user.entity.User;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerOrderRepository orderRepository;
    private final UserContextService userContextService;
    private final CommissionService commissionService;

    @Transactional
    public PaymentDto processPayment(PaymentRequest request) {
        User user = userContextService.getCurrentUser();
        CustomerOrder order = orderRepository.findByIdAndUser(request.getOrderId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ValidationException("Cette commande a déjà été payée ou annulée");
        }

        if (paymentRepository.existsByOrder(order)) {
            throw new ValidationException("Paiement déjà effectué pour cette commande");
        }

        validatePaymentMethod(request);

        Payment payment = Payment.builder()
                .order(order)
                .user(user)
                .amount(order.getTotalAmount())
                .method(request.getMethod())
                .status(PaymentStatus.COMPLETED)
                .transactionRef("TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
        paymentRepository.save(payment);

        order.setStatus(OrderStatus.CONFIRMED);
        order.getSellerOrders().forEach(so -> so.setStatus(OrderStatus.CONFIRMED));
        orderRepository.save(order);

        commissionService.processCommissions(payment, order);

        return toDto(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDto getPaymentForOrder(Long orderId) {
        User user = userContextService.getCurrentUser();
        CustomerOrder order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable"));

        return toDto(payment);
    }

    private void validatePaymentMethod(PaymentRequest request) {
        if (request.getMethod() == com.nexora.common.enums.PaymentMethod.CARD) {
            String card = request.getCardNumber();
            if (card == null || card.replaceAll("\\s", "").length() < 13) {
                throw new ValidationException("Numéro de carte invalide");
            }
        }
    }

    private PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionRef(payment.getTransactionRef())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
