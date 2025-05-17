package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.constant.ReceiverType;
import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.NotificationUser;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class NotificationUserSpecification {
    public static Specification<NotificationUser> isRead(Boolean isRead){
        if(isRead == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
          root.get("isRead"), isRead
        );
    }

    public static Specification<NotificationUser> hasReceiverUsername(String username){
        if(username == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("receiver").get("username"), username
        );
    }

    public static Specification<NotificationUser> hasGroupReceiver(RoleName roleName) {
        return (root, query, cb) -> {
            Predicate rolePredicate = cb.equal(root.get("receiverRole").get("roleName"), roleName);
            Predicate receiverTypePredicate = cb.equal(root.get("receiverType"), ReceiverType.GROUP);
            return cb.and(rolePredicate, receiverTypePredicate);
        };
    }

    public static  Specification<NotificationUser> hasGroupOrUsername(String username, RoleName roleName){
        return ((root, query, criteriaBuilder) -> {
            Join<NotificationUser, User> userJoin = root.join("receiver", JoinType.LEFT);
            Join<NotificationUser, Role> roleJoin = root.join("receiverRole", JoinType.LEFT);

            Predicate usernamePredicate = criteriaBuilder.equal(userJoin.get("username"), username);

            Predicate rolePredicate = criteriaBuilder.equal(roleJoin.get("roleName"), roleName.name());
            Predicate receiverTypePredicate = criteriaBuilder.equal(root.get("receiverType"), ReceiverType.GROUP.name());
            Predicate groupPredicate = criteriaBuilder.and(rolePredicate, receiverTypePredicate);

            return criteriaBuilder.or(usernamePredicate, groupPredicate);
        });
    }
}
