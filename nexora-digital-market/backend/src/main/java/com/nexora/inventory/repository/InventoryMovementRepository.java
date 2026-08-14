package com.nexora.inventory.repository;

import com.nexora.inventory.entity.InventoryMovement;
import com.nexora.seller.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    Page<InventoryMovement> findBySellerOrderByCreatedAtDesc(Seller seller, Pageable pageable);
}
