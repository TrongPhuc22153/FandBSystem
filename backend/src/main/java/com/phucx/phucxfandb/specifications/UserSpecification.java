package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasRole(RoleName roleName) {
        return (root, query, builder) -> {
            if(roleName == null) return null;
            Join<User, Role> roleJoin = root.join("roles");
            return builder.equal(roleJoin.get("roleName"), roleName);
        };
    }

    public static Specification<User> isEnabled(Boolean enabled){
        if(enabled == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), enabled);
    }

    public static Specification<User> hasUsername(String username){
        if(username == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
    }

    public static Specification<User> hasEmail(String email){
        if(email == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<User> searchByUsername(String search){
        if(search==null || search.trim().isBlank()) return null;
        String searchTerm = "%" + search + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("username")), searchTerm
        );
    }

    public static Specification<User> searchByEmail(String search){
        if(search == null || search.trim().isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%"; // Convert search term to lower case once
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")), searchTerm
        );
    }

    public static Specification<User> searchByFullName(String search){
        if(search == null || search.trim().isEmpty()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.concat(
                criteriaBuilder.concat(
                        criteriaBuilder.coalesce(root.get("firstName"), ""),
                        criteriaBuilder.literal(" ")
                ),
                criteriaBuilder.coalesce(root.get("lastName"), "")
        )), searchTerm));
    }

    public static Specification<User> hasSearch(String search){
        if(search == null || search.trim().isEmpty()) return null;
        return Specification.where(searchByFullName(search))
                .or(searchByUsername(search))
                .or(searchByEmail(search));
    }
}
