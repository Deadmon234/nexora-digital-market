package com.nexora.favorite.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.favorite.dto.FavoriteDto;
import com.nexora.favorite.entity.Favorite;
import com.nexora.favorite.repository.FavoriteRepository;
import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductImage;
import com.nexora.product.entity.ProductOffer;
import com.nexora.product.repository.ProductOfferRepository;
import com.nexora.product.repository.ProductRepository;
import com.nexora.user.entity.User;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;
    private final UserContextService userContextService;

    @Transactional(readOnly = true)
    public List<FavoriteDto> getMyFavorites() {
        User user = userContextService.getCurrentUser();
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> isFavorite(Long productId) {
        User user = userContextService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        return Map.of("favorited", favoriteRepository.existsByUserAndProduct(user, product));
    }

    @Transactional
    public FavoriteDto addFavorite(Long productId) {
        User user = userContextService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            return favoriteRepository.findByUserAndProduct(user, product)
                    .map(this::toDto)
                    .orElseThrow();
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();

        return toDto(favoriteRepository.save(favorite));
    }

    @Transactional
    public void removeFavorite(Long productId) {
        User user = userContextService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        favoriteRepository.deleteByUserAndProduct(user, product);
    }

    private FavoriteDto toDto(Favorite favorite) {
        Product product = favorite.getProduct();
        String imageUrl = product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getUrl)
                .orElse(null);

        BigDecimal minPrice = productOfferRepository
                .findByProductAndActiveTrueOrderByPriceAsc(product).stream()
                .findFirst()
                .map(ProductOffer::getPrice)
                .orElse(null);

        return FavoriteDto.builder()
                .id(favorite.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .imageUrl(imageUrl)
                .minPrice(minPrice)
                .build();
    }
}
