package com.nexora.cart.service;

import com.nexora.cart.dto.*;
import com.nexora.cart.entity.Cart;
import com.nexora.cart.entity.CartItem;
import com.nexora.cart.repository.CartItemRepository;
import com.nexora.cart.repository.CartRepository;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductImage;
import com.nexora.product.entity.ProductOffer;
import com.nexora.product.repository.ProductOfferRepository;
import com.nexora.user.entity.User;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOfferRepository productOfferRepository;
    private final UserContextService userContextService;

    @Transactional
    public CartDto getMyCart() {
        Cart cart = getOrCreateCart(userContextService.getCurrentUser());
        return toDto(cart);
    }

    @Transactional
    public CartDto addItem(AddCartItemRequest request) {
        User user = userContextService.getCurrentUser();
        Cart cart = getOrCreateCart(user);

        ProductOffer offer = productOfferRepository.findById(request.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        if (!offer.isActive()) {
            throw new ValidationException("Cette offre n'est plus disponible");
        }
        if (offer.getStock() == null || offer.getStock() < request.getQuantity()) {
            throw new ValidationException("Stock insuffisant");
        }

        CartItem item = cartItemRepository.findByCartAndProductOffer(cart, offer)
                .orElse(null);

        if (item != null) {
            int newQty = item.getQuantity() + request.getQuantity();
            if (offer.getStock() < newQty) {
                throw new ValidationException("Stock insuffisant");
            }
            item.setQuantity(newQty);
            item.setUnitPrice(offer.getPrice());
        } else {
            item = CartItem.builder()
                    .cart(cart)
                    .productOffer(offer)
                    .quantity(request.getQuantity())
                    .unitPrice(offer.getPrice())
                    .build();
            cart.getItems().add(item);
        }

        cartRepository.save(cart);
        return toDto(cart);
    }

    @Transactional
    public CartDto updateItem(Long itemId, UpdateCartItemRequest request) {
        CartItem item = requireOwnedItem(itemId);
        ProductOffer offer = item.getProductOffer();

        if (!offer.isActive()) {
            throw new ValidationException("Cette offre n'est plus disponible");
        }
        if (offer.getStock() < request.getQuantity()) {
            throw new ValidationException("Stock insuffisant");
        }

        item.setQuantity(request.getQuantity());
        item.setUnitPrice(offer.getPrice());
        cartItemRepository.save(item);
        return toDto(item.getCart());
    }

    @Transactional
    public CartDto removeItem(Long itemId) {
        CartItem item = requireOwnedItem(itemId);
        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartRepository.save(cart);
        return toDto(cart);
    }

    @Transactional
    public CartDto clearCart() {
        Cart cart = getOrCreateCart(userContextService.getCurrentUser());
        cart.getItems().clear();
        cartRepository.save(cart);
        return toDto(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = Cart.builder().user(user).build();
            return cartRepository.save(cart);
        });
    }

    private CartItem requireOwnedItem(Long itemId) {
        User user = userContextService.getCurrentUser();
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Article introuvable");
        }
        return item;
    }

    private CartDto toDto(Cart cart) {
        List<CartItemDto> items = cart.getItems().stream()
                .map(this::toItemDto)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemDto::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = items.stream().mapToInt(CartItemDto::getQuantity).sum();

        return CartDto.builder()
                .id(cart.getId())
                .items(items)
                .itemCount(count)
                .totalAmount(total)
                .build();
    }

    private CartItemDto toItemDto(CartItem item) {
        ProductOffer offer = item.getProductOffer();
        Product product = offer.getProduct();
        String imageUrl = product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getUrl)
                .orElse(null);

        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemDto.builder()
                .id(item.getId())
                .offerId(offer.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .sellerName(offer.getSeller().getCompanyName())
                .imageUrl(imageUrl)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(lineTotal)
                .availableStock(offer.getStock())
                .build();
    }
}
