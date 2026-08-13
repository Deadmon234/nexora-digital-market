package com.marketplace.seller.repository;

import com.marketplace.seller.entity.VendorProfile;
import com.marketplace.seller.entity.VendorStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {

    Optional<VendorProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Page<VendorProfile> findByStatus(VendorStatus status, Pageable pageable);
}
