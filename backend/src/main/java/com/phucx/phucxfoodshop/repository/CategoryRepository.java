package com.phucx.phucxfoodshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.phucxfoodshop.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("""
        SELECT c FROM Category c WHERE c.categoryName=?1
            """)
    Optional<Category> findByCategoryName(String categoryName);

    @Procedure("AddCategory")
    Boolean addCategory(String categoryname, String description, String picture);

    @Modifying
    @Transactional
    @Query(""" 
        UPDATE Category SET categoryName=?1, description=?2, picture=?3 \
        WHERE categoryID=?4
            """)
    Integer updateCategory(String categoryName, String description, String picture, Integer categoryID);

    @Query("""
        SELECT c FROM Category c WHERE categoryName LIKE ?1
            """)
    List<Category> findByCategoryNameLike(String categoryName);
    
}
