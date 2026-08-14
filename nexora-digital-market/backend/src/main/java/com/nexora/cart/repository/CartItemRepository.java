package com.nexora.cart.repository;

import com.nexora.cart.entity.Cart;
import com.nexora.cart.entity.CartItem;
import com.nexora.product.entity.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductOffer(Cart cart, ProductOffer productOffer);
}
