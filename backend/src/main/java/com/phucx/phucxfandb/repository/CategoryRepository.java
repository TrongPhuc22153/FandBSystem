package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional(readOnly = true)
    Optional<Category> findByCategoryNameAndIsDeletedFalse(String categoryName);

    @Transactional(readOnly = true)
    Page<Category> findByIsDeletedFalse(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<Category> findByCategoryIdAndIsDeletedFalse(long categoryId);

    @Transactional(readOnly = true)
    List<Category> findByCategoryNameLike(String categoryName);

    @Transactional(readOnly = true)
    boolean existsByCategoryName(String categoryName);
    
}
