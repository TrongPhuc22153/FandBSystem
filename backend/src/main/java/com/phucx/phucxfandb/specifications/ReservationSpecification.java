package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReservationSpecification {

    public static Specification<Reservation> hasDate(LocalDate startDate, LocalDate endDate){
        if(startDate == null || endDate == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("date"), startDate, endDate));
    }

    public static Specification<Reservation> hasStatus(ReservationStatus status){
        if(status == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Reservation> hasCustomerUsername(String username){
        if(username == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("profile").get("user").get("username"), username);
    }
}
