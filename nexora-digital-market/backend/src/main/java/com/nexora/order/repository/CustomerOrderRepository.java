package com.nexora.order.repository;

import com.nexora.common.enums.OrderStatus;
import com.nexora.order.entity.CustomerOrder;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserOrderByCreatedAtDesc(User user);
    Optional<CustomerOrder> findByIdAndUser(Long id, User user);
    boolean existsByOrderNumber(String orderNumber);
    List<CustomerOrder> findAllByOrderByCreatedAtDesc();
    long countByStatus(OrderStatus status);
}
