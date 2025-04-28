package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String>{
    @Transactional(readOnly = true)
    boolean existsByDiscountCode(String code);

    @Transactional(readOnly = true)
    Page<Discount> findByIsDeletedFalse(Pageable pageable);

    @Transactional(readOnly = true)
    Page<Discount> findByProductsProductIdAndIsDeletedFalse(long productId, Pageable pageable);

    @Transactional(readOnly = true)
    List<Discount> findByProductsProductIdAndActiveTrueAndDiscountIdIn(long productId, List<String> discountIds);

}
