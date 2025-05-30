package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class PaymentSpecification {

    public static Specification<Payment> hasStatus(PaymentStatus status){
        if(status==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Payment> hasTableNumber(Integer tableNumber){
        if(tableNumber==null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("order").get("waitList").get("table").get("tableNumber"), tableNumber
        );
    }

    public static Specification<Payment> searchByOrderId(String search){
        if(search==null || search.isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("order").get("orderId")),  searchTerm
        );
    }

    public static Specification<Payment> searchByOrderContactName(String search){
        if(search==null || search.isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> {
            Join<Payment, Order> orderJoin = root.join("order", JoinType.INNER);
            Join<Order, Customer> customerJoin = orderJoin.join("customer", JoinType.LEFT);
            Join<Order, WaitList> waitListJoin = orderJoin.join("waitList", JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(customerJoin.get("contactName")), searchTerm),
                    criteriaBuilder.like(criteriaBuilder.lower(waitListJoin.get("contactName")), searchTerm)
            );
        };
    }

    public static Specification<Payment> searchByReservationId(String search){
        if(search==null || search.isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("reservation").get("reservationId")),  searchTerm
        );
    }


    public static Specification<Payment> searchByOrderPhone(String search){
        if(search==null || search.isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> {
            Join<Payment, Order> orderJoin = root.join("order", JoinType.INNER);
            Join<Order, Customer> customerJoin = orderJoin.join("customer", JoinType.LEFT);
            Join<Order, WaitList> waitListJoin = orderJoin.join("waitList", JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(customerJoin.get("profile").get("phone")), searchTerm),
                    criteriaBuilder.like(criteriaBuilder.lower(waitListJoin.get("phone")), searchTerm)
            );
        };
    }

}
