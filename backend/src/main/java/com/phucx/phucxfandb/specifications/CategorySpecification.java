package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasIsDeleted(Boolean isDeleted) {
        if (isDeleted == null) return null;
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), isDeleted);
    }

    public static Specification<Category> hasSearch(String search) {
        if(search == null || search.trim().isEmpty()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("categoryName")), searchTerm),
                cb.like(cb.lower(root.get("description")), searchTerm)
        );
    }
}
