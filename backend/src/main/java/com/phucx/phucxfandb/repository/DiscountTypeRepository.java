package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface DiscountTypeRepository extends JpaRepository<DiscountType, Long>{

    @Transactional(readOnly = true)
    Page<DiscountType> findByIsDeletedFalse(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<DiscountType> findByDiscountTypeIdAndIsDeletedFalse(long id);

    @Transactional(readOnly = true)
    boolean existsByDiscountType(String name);
}
