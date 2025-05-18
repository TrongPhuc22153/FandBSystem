package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.ReservationStatus;
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
}
