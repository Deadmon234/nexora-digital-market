package com.nexora.user.repository;

import com.nexora.user.entity.Address;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
