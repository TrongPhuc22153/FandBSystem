package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
       @EntityGraph(attributePaths = {"category", "productSize"})
       Optional<Product> findByProductIdAndIsDeletedFalse(long productId);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByIsDeletedFalse(Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findAll(Specification<Product> spec, Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findAll(Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Optional<Product> findByProductIdAndIsDeleted(long productId, boolean isDeleted);

       boolean existsByProductName(String productName);

       boolean existsByCategoryCategoryIdAndIsDeletedFalse(long categoryId);
}
