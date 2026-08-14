package com.nexora.payment.repository;

import com.nexora.order.entity.CustomerOrder;
import com.nexora.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(CustomerOrder order);
    boolean existsByOrder(CustomerOrder order);
}
