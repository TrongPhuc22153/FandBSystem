package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import org.springframework.data.jpa.domain.Specification;

public class WaitListSpecification {
    public static Specification<TableOccupancy> hasStatus(TableOccupancyStatus status){
        if(status == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
    }
}
