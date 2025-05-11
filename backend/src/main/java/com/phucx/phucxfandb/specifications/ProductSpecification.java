package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasSearchValue(String searchValue) {
        return (root, query, cb) -> {
            if (searchValue == null || searchValue.isEmpty()) return null;
            String searchTerm = "%" + searchValue.replaceAll("\\+", " ").toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("productName")), searchTerm),
                    cb.like(cb.lower(root.get("description")), searchTerm)
            );
        };
    }

    public static Specification<Product> hasIsDeleted(Boolean isDeleted) {
        return (root, query, cb) -> {
            if (isDeleted == null) return null;
            return cb.equal(root.get("isDeleted"), isDeleted);
        };
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }

    public static Specification<Product> isFeatured(Boolean isFeatured) {
        return (root, query, cb) -> {
            if (isFeatured == null) return null;
            return cb.equal(root.get("isFeatured"), isFeatured);
        };
    }
}
