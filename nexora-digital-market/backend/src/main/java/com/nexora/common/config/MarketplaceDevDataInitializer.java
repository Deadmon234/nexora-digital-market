package com.nexora.common.config;

import com.nexora.common.enums.RoleName;
import com.nexora.common.enums.SellerStatus;
import com.nexora.common.enums.ShopStatus;
import com.nexora.product.entity.*;
import com.nexora.product.repository.*;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.shop.entity.Shop;
import com.nexora.shop.repository.ShopRepository;
import com.nexora.user.entity.Role;
import com.nexora.user.entity.User;
import com.nexora.user.repository.RoleRepository;
import com.nexora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class MarketplaceDevDataInitializer {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SellerRepository sellerRepository;
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initMarketplaceData() {
        return args -> seedIfEmpty();
    }

    @Transactional
    protected void seedIfEmpty() {
        if (productRepository.count() > 0) {
            ensureDemoAdmin();
            return;
        }

        log.info("Initialisation des données marketplace de démonstration...");

        Category smartphones = saveCategory("Smartphones", "smartphones", "Téléphones et accessoires", null);
        Category laptops = saveCategory("Ordinateurs", "ordinateurs", "PC portables et fixes", null);
        saveCategory("Android", "android", null, smartphones);
        saveCategory("iPhone", "iphone", null, smartphones);

        Brand apple = saveBrand("Apple", "apple", "Technologie Apple");
        Brand samsung = saveBrand("Samsung", "samsung", "Électronique Samsung");
        Brand dell = saveBrand("Dell", "dell", "Ordinateurs Dell");

        Seller seller = ensureDemoSeller();

        Product iphone = saveProduct(
                "iPhone 15 Pro",
                "iphone-15-pro",
                "Smartphone Apple dernière génération, puce A17 Pro, appareil photo avancé.",
                smartphones, apple,
                "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800"
        );
        Product galaxy = saveProduct(
                "Samsung Galaxy S24",
                "samsung-galaxy-s24",
                "Flagship Samsung avec IA Galaxy, écran AMOLED 120Hz.",
                smartphones, samsung,
                "https://images.unsplash.com/photo-1610945265064-75e343ea6368?w=800"
        );
        Product xps = saveProduct(
                "Dell XPS 15",
                "dell-xps-15",
                "Ultrabook premium Intel Core i7, 16 Go RAM, écran OLED.",
                laptops, dell,
                "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800"
        );

        saveOffer(iphone, seller, new BigDecimal("1199.99"), 25, "Neuf");
        saveOffer(galaxy, seller, new BigDecimal("899.99"), 40, "Neuf");
        saveOffer(xps, seller, new BigDecimal("1499.00"), 15, "Neuf");

        log.info("Données marketplace de démonstration créées.");
        ensureDemoAdmin();
    }

    private Category saveCategory(String name, String slug, String description, Category parent) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .parent(parent)
                .active(true)
                .build());
    }

    private Brand saveBrand(String name, String slug, String description) {
        return brandRepository.save(Brand.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .active(true)
                .build());
    }

    private Product saveProduct(String name, String slug, String description, Category category, Brand brand, String imageUrl) {
        Product product = Product.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .category(category)
                .brand(brand)
                .active(true)
                .build();

        ProductImage image = ProductImage.builder()
                .product(product)
                .url(imageUrl)
                .altText(name)
                .displayOrder(0)
                .primary(true)
                .build();
        product.getImages().add(image);

        return productRepository.save(product);
    }

    private void saveOffer(Product product, Seller seller, BigDecimal price, int stock, String condition) {
        productOfferRepository.save(ProductOffer.builder()
                .product(product)
                .seller(seller)
                .price(price)
                .stock(stock)
                .conditionLabel(condition)
                .active(true)
                .build());
    }

    private Seller ensureDemoSeller() {
        return userRepository.findByEmail("vendeur@nexora.dev")
                .flatMap(user -> sellerRepository.findByUser(user))
                .orElseGet(() -> {
                    Role sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER)
                            .orElseThrow();

                    User user = userRepository.save(User.builder()
                            .email("vendeur@nexora.dev")
                            .password(passwordEncoder.encode("password123"))
                            .firstName("Demo")
                            .lastName("Vendeur")
                            .enabled(true)
                            .roles(Set.of(sellerRole))
                            .build());

                    Seller seller = sellerRepository.save(Seller.builder()
                            .user(user)
                            .companyName("Nexora Tech Store")
                            .status(SellerStatus.APPROVED)
                            .commissionRate(new BigDecimal("10.00"))
                            .build());

                    shopRepository.save(Shop.builder()
                            .seller(seller)
                            .name("Nexora Tech Store")
                            .slug("nexora-tech-store")
                            .description("Boutique de démonstration Nexora")
                            .status(ShopStatus.APPROVED)
                            .active(true)
                            .build());

                    return seller;
                });
    }

    private void ensureDemoAdmin() {
        if (userRepository.findByEmail("admin@nexora.dev").isPresent()) {
            return;
        }
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow();
        userRepository.save(User.builder()
                .email("admin@nexora.dev")
                .password(passwordEncoder.encode("password123"))
                .firstName("Demo")
                .lastName("Admin")
                .enabled(true)
                .roles(Set.of(adminRole))
                .build());
        log.info("Compte administrateur de démonstration créé : admin@nexora.dev");
    }
}
