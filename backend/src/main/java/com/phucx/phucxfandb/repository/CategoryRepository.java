package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Page<Category> findByIsDeletedFalse(Pageable pageable);

    Optional<Category> findByCategoryIdAndIsDeletedFalse(long categoryId);

    Optional<Category> findByCategoryIdAndIsDeleted(long categoryId, boolean isDeleted);

    boolean existsByCategoryName(String categoryName);
}
