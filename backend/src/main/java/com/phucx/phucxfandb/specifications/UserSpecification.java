package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.constant.RoleName;
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
}
