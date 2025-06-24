package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> hasCustomerUsername(String username){
        if(username==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("profile").get("user").get("username"), username);
    }
    public static Specification<Order> hasStatuses(List<OrderStatus> statuses){
        if(statuses==null) return null;
        return (root, query, criteriaBuilder) -> (root.get("status").in(statuses));
    }
    public static Specification<Order> hasType(OrderType type){
        if(type==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Order> hasOrderDateBetween(LocalDate startDate, LocalDate endDate){
        if(startDate==null || endDate==null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(
                        root.get("orderDate"),
                        startDate,
                        endDate
                ));
    }

    public static Specification<Order> searchOrders(String search){
        if(search == null || search.isBlank()) return null;
        String searchTerm = "%" + search + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("orderId"), searchTerm));
    }


}
