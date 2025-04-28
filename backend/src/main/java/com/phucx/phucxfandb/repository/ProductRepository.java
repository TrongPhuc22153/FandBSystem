package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
       @EntityGraph(attributePaths = {"category", "productSize"})
       Optional<Product> findByProductIdAndIsDeletedFalse(long productId);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByCategoryCategoryNameAndIsDeletedFalse(String categoryName, Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByIsDeletedFalse(Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Optional<Product> findByProductIdAndIsDeletedFalse(Long productId);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByCategoryCategoryIdAndIsDeletedFalse(long categoryId, Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByProductNameContainingIgnoreCaseAndIsDeletedFalse(String productName, Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByIsFeaturedTrueAndIsDeletedFalse(Pageable pageable);

       boolean existsByProductName(String productName);
}
