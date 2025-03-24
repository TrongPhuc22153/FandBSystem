package com.phucx.phucxfoodshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.CurrentProduct;


@Repository
public interface CurrentProductRepository extends JpaRepository<CurrentProduct, Integer>{
    @Query("""
        SELECT c FROM CurrentProduct c \
        WHERE productName LIKE ?1     
        """)
    Page<CurrentProduct> searchCurrentProductsByProductName(String productName, Pageable page);

    Page<CurrentProduct> findByCategoryName(String categoryName, Pageable page);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM `current product list` \
        where CategoryName LIKE ?2 AND ProductID<>?1 \
        ORDER BY RAND()
        """)
    Page<CurrentProduct> findRandomLikeCategoryNameWithoutProductID(int productID, String categoryName, Pageable page);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM `current product list` \
        ORDER BY RAND()
        """)
    Page<CurrentProduct> findProductsRandom(Pageable page);

    @Query("""
        SELECT c FROM CurrentProduct c \
        WHERE c.productID IN ?1 \
        ORDER BY c.productID Asc
            """)
    List<CurrentProduct> findAllByProductIDOrderByProductIDAsc(List<Integer> productIDs);
}
