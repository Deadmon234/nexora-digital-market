package com.nexora.product.specification;

import com.nexora.product.entity.Brand;
import com.nexora.product.entity.Category;
import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductOffer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> withFilters(
            String query,
            String categorySlug,
            String brandSlug,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));

            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.toLowerCase().trim() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            if (categorySlug != null && !categorySlug.isBlank()) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(cb.equal(categoryJoin.get("slug"), categorySlug));
                predicates.add(cb.isTrue(categoryJoin.get("active")));
            }

            if (brandSlug != null && !brandSlug.isBlank()) {
                Join<Product, Brand> brandJoin = root.join("brand", JoinType.INNER);
                predicates.add(cb.equal(brandJoin.get("slug"), brandSlug));
                predicates.add(cb.isTrue(brandJoin.get("active")));
            }

            if (minPrice != null || maxPrice != null) {
                Join<Product, ProductOffer> offerJoin = root.join("offers", JoinType.INNER);
                predicates.add(cb.isTrue(offerJoin.get("active")));
                predicates.add(cb.gt(offerJoin.get("stock"), 0));
                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(offerJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(offerJoin.get("price"), maxPrice));
                }
                cq.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
