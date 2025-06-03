package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
       @EntityGraph(attributePaths = {"category", "productSize"})
       Optional<Product> findByProductIdAndIsDeletedFalse(long productId);

       @EntityGraph(attributePaths = {"category"})
       Page<Product> findByIsDeletedFalse(Pageable pageable);

       @NonNull
       @EntityGraph(attributePaths = {"category"})
       Page<Product> findAll(@Nullable Specification<Product> spec, @NonNull Pageable pageable);

       @NonNull
       @EntityGraph(attributePaths = {"category"})
       Page<Product> findAll(@NonNull Pageable pageable);

       @EntityGraph(attributePaths = {"category"})
       Optional<Product> findByProductIdAndIsDeleted(long productId, boolean isDeleted);

       boolean existsByProductName(String productName);

       boolean existsByCategoryCategoryIdAndIsDeletedFalse(long categoryId);

       @Query("""
        SELECT p FROM Product p \
            LEFT JOIN OrderDetail oi ON oi.product = p \
            LEFT JOIN oi.order o ON o.customer.profile.user.username = :username \
            LEFT JOIN MenuItem mi ON mi.product = p \
            LEFT JOIN mi.reservation res ON res.customer.profile.user.username = :username \
            LEFT JOIN p.ratings r ON r.customer.profile.user.username = :username \
        WHERE p.id = :productId \
            AND p.isDeleted = :isDeleted \
            AND ( \
                (o.status = :orderStatus AND o.customer.profile.user.username = :username) \
                OR (res.status = :reservationStatus AND res.customer.profile.user.username = :username) \
            )
        """)
       @EntityGraph(attributePaths = {"ratings", "ratings.customer.profile", "ratings.customer.profile.user"})
       Optional<Product> findOptionalRatingProductUsername(
               @Param("username") String username,
               @Param("productId") long productId,
               @Param("orderStatus") OrderStatus orderStatus,
               @Param("reservationStatus") ReservationStatus reservationStatus,
               @Param("isDeleted") boolean isDeleted
       );

       @Query("""
           SELECT p.productName, SUM(
               COALESCE(od.quantity, 0) + COALESCE(mi.quantity, 0)
           )
           FROM Product p
           LEFT JOIN OrderDetail od ON od.product = p
           LEFT JOIN MenuItem mi ON mi.product = p
           GROUP BY p.productName
           ORDER BY SUM(COALESCE(od.quantity, 0) + COALESCE(mi.quantity, 0)) DESC
       """)
       List<Object[]> getTopSellingProducts(
               @Param("startOfWeek") LocalDateTime startOfWeek,
               @Param("endOfWeek") LocalDateTime endOfWeek,
               Pageable pageable);

       @Query("""
           SELECT p.productName, SUM(od.quantity)
           FROM OrderDetail od
           JOIN od.product p
           JOIN od.order o
           WHERE o.orderDate BETWEEN :start AND :end
           GROUP BY p.productName
       """)
       List<Object[]> getProductSalesFromOrders(
               @Param("start") LocalDateTime start,
               @Param("end") LocalDateTime end
       );

       @Query("""
           SELECT p.productName, SUM(mi.quantity)
           FROM MenuItem mi
           JOIN mi.product p
           JOIN mi.reservation r
           WHERE r.date BETWEEN :start AND :end
           GROUP BY p.productName
       """)
       List<Object[]> getProductSalesFromReservations(
               @Param("start") LocalDate start,
               @Param("end") LocalDate end
       );

       @Query("""
               SELECT c.categoryName, SUM(od.quantity)
               FROM OrderDetail od
               JOIN od.product p
               JOIN p.category c
               JOIN od.order o
               WHERE c.isDeleted = false AND p.isDeleted = false
                   AND o.orderDate BETWEEN :start AND :end
               GROUP BY c.categoryName
           """)
       List<Object[]> getCategorySalesFromOrders(
               @Param("start") LocalDateTime start,
               @Param("end") LocalDateTime end
       );

       @Query("""
               SELECT c.categoryName, SUM(mi.quantity)
               FROM MenuItem mi
               JOIN mi.product p
               JOIN p.category c
               JOIN mi.reservation r
               WHERE c.isDeleted = false AND p.isDeleted = false
                   AND r.date BETWEEN :start AND :end
               GROUP BY c.categoryName
           """)
       List<Object[]> getCategorySalesFromReservations(
               @Param("start") LocalDate start,
               @Param("end") LocalDate end
       );



}
