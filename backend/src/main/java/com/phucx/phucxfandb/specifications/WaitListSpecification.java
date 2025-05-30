package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.enums.WaitListStatus;
import com.phucx.phucxfandb.entity.WaitList;
import org.springframework.data.jpa.domain.Specification;

public class WaitListSpecification {
    public static Specification<WaitList> hasStatus(WaitListStatus status){
        if(status == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
    }
}
