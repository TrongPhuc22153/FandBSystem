package com.phucx.phucxfoodshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

       @Procedure(name = "UpdateProductUnitsInStock")
       Boolean updateProductUnitsInStock(Integer productID, Integer unitsInStock);

       @Procedure(name = "UpdateProductsUnitsInStock")
       Boolean updateProductsUnitsInStock(String productIDs, String unitsInStocks);

       Page<Product> findByProductNameLike(String productName, Pageable page);

       List<Product> findByProductName(String productName);

       Page<Product> findByProductName(String productName, Pageable page);

       @Query("""
              SELECT p \
              FROM Product p JOIN Category c ON p.categoryID=c.categoryID \
              WHERE c.categoryName=:categoryName \
              """)
       Page<Product> findByCategoryName(String categoryName, Pageable page);

       @Query("""
              SELECT p \
              FROM Product p JOIN Category c ON p.categoryID=c.categoryID \
              WHERE c.categoryName=?1 AND p.productName=?2
              """)
       Product findByCategoryNameAndProductName(String categoryName, String productName);

       @Query("""
              SELECT p \
              FROM Product p JOIN Category c ON p.categoryID=c.categoryID \
              WHERE c.categoryName LIKE ?1       
              """)
       Page<Product> findByCategoryNameLike(String categoryName, Pageable page);

       Optional<Product> findByProductIDAndDiscontinued(Integer productID, Boolean discontinued);
}
