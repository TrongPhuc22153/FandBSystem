package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.springframework.data.jpa.domain.Specification;

public class TableSpecification {
    public static Specification<ReservationTable> hasStatus(TableStatus status){
        if(status == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<ReservationTable> hasTableNumber(Integer tableNumber){
        if(tableNumber == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableNumber"), tableNumber);
    }

    public static Specification<ReservationTable> hasIsDeleted(Boolean isDeleted){
        if(isDeleted == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }
}
