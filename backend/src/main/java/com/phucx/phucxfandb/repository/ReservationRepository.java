package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Reservation;
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
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {
    @NonNull
    @EntityGraph(attributePaths = {"menuItems", "menuItems.product", "customer", "customer.profile", "customer.profile.user", "employee", "employee.profile", "employee.profile.user", "table"})
    Page<Reservation> findAll(@Nullable Specification<Reservation> spec, @NonNull Pageable pageable);

    Optional<Reservation> findByReservationIdAndCustomerProfileUserUsername(String reservationId, String username);

    Optional<Reservation> findByReservationIdAndEmployeeProfileUserUsername(String reservationId, String username);

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.table.tableId = :tableId
                AND r.date = :date
                AND r.startTime <= :endTime AND r.endTime > :startTime
                AND r.status NOT IN ('CANCELED', 'COMPLETED')
            """)
    List<Reservation> findOverlappingReservations(
            @Param("tableId") String tableId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.date = :date
                AND r.startTime <= :time
                AND r.endTime > :time
                AND r.table.tableId IN :tableIds
                AND r.status NOT IN ('CANCELED', 'COMPLETED')
            """)
    List<Reservation> findActiveReservations(
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("tableIds") Collection<String> tableIds);


    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.date = :date
                AND r.startTime > :time
                AND r.startTime <= :futureTime
                AND r.table.tableId IN :tableIds
                AND r.status NOT IN ('CANCELED', 'COMPLETED')
            """)
    List<Reservation> findUpcomingReservations(
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("futureTime") LocalTime futureTime,
            @Param("tableIds") Collection<String> tableIds);

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.date BETWEEN :startOfDay AND :endOfDay
            """)
    Long countReservations(
            @Param("startOfDay") LocalDate startOfDay,
            @Param("endOfDay") LocalDate endOfDay
    );
}
