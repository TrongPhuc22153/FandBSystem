package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.entity.NotificationUser;
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
}
