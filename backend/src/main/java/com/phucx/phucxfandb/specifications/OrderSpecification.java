package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> hasCustomerUsername(String username){
        if(username==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("profile").get("user").get("username"), username);
    }
    public static Specification<Order> hasStatus(OrderStatus status){
        if(status==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
    public static Specification<Order> hasType(OrderType type){
        if(type==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }


}
