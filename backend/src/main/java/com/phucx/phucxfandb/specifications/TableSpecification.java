package com.phucx.phucxfandb.specifications;

import com.phucx.phucxfandb.entity.TableEntity;
import org.springframework.data.jpa.domain.Specification;

public class TableSpecification {
    public static Specification<TableEntity> hasTableNumber(Integer tableNumber){
        if(tableNumber == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableNumber"), tableNumber);
    }

    public static Specification<TableEntity> hasIsDeleted(Boolean isDeleted){
        if(isDeleted == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }

    public static Specification<TableEntity> searchByLocation(String search){
        if(search == null || search.trim().isEmpty()) return null;
        String searchTerm = "%" + search.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("location")), searchTerm
        ));
    }

    public static Specification<TableEntity> hasSearch(String search){
        if (search == null || search.trim().isEmpty()) return null;
        return Specification.where(searchByLocation(search));
    }

}
