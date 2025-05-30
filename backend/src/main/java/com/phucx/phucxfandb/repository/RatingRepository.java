package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {

    @EntityGraph(attributePaths = {"customer.profile.user"})
    Page<Rating> findByProductProductId(long productId, Pageable pageable);

    Optional<Rating> findByIdAndCustomerProfileUserUsername(String ratingId, String username);

    Optional<Rating> findByProductProductIdAndCustomerProfileUserUsername(long productId, String username);

    @Query("""
            SELECT COALESCE(AVG(r.score), 0) \
                FROM Rating r \
                WHERE r.product.productId = ?1
            """)
    BigDecimal findAverageRatingByProductId(long productId);

    @Query("""
        SELECT r FROM Rating r \
            LEFT JOIN r.product p \
            LEFT JOIN OrderDetail oi ON oi.product = p \
            LEFT JOIN oi.order o ON o.customer = r.customer \
            LEFT JOIN MenuItem mi ON mi.product = p \
            LEFT JOIN mi.reservation res ON res.customer = r.customer \
                AND res.customer.profile.user.username = :username \
        WHERE p.productId = :productId \
            AND ( \
                (o.status = :orderStatus AND o.customer.profile.user.username = :username) \
                OR (res.status = :reservationStatus AND res.customer.profile.user.username = :username) \
            ) \
        """)
    Optional<Rating> findOptionalProductRatingUsername(
            @Param("username") String username,
            @Param("productId") long productId,
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("reservationStatus") ReservationStatus reservationStatus
    );
}
