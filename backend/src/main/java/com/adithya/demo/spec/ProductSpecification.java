package com.adithya.demo.spec;

import com.adithya.demo.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> hasMinPrice(Double minPrice) {
        return (root, query, cb) -> minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(Double maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}