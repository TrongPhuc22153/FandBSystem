package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ReservationSpecification {

    public static Specification<Reservation> hasDate(LocalDate startDate, LocalDate endDate){
        if(startDate == null || endDate == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("date"), startDate, endDate));
    }

    public static Specification<Reservation> hasStatuses(List<ReservationStatus> statuses){
        if(statuses == null) return null;
        return (root, query, criteriaBuilder) -> root.get("status").in(statuses);
    }

    public static Specification<Reservation> hasCustomerUsername(String username){
        if(username == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("customer").get("profile").get("user").get("username"), username);
    }

    public static Specification<Reservation> searchReservations(String search){
        if(search == null || search.isBlank()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("reservationId"), searchTerm),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("contactName")), searchTerm))
        );
    }

}
