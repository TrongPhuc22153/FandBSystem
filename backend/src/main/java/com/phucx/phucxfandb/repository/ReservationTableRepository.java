package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, String>, JpaSpecificationExecutor<ReservationTable> {

    boolean existsByTableNumber(int tableNumber);

    Page<ReservationTable> findByIsDeletedFalse(Pageable pageable);

    Optional<ReservationTable> findByTableIdAndIsDeletedFalse(String tableId);

    List<ReservationTable> findByStatusAndIsDeletedFalse(TableStatus status);

    @Query("SELECT rt FROM ReservationTable rt " +
            "WHERE rt.status = :status " +
            "AND rt.capacity >= :capacity " +
            "AND rt.isDeleted = false " +
            "AND rt.tableId NOT IN (" +
            "   SELECT r.table.tableId FROM Reservation r " +
            "   WHERE r.startTime < :endTime AND r.endTime > :startTime" +
            ")")
    List<ReservationTable> findAvailableTablesForReservation(
            @Param("status") TableStatus status,
            @Param("capacity") Integer capacity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            SELECT t FROM ReservationTable t \
            WHERE t.capacity >= :numberOfGuests \
            AND t.isDeleted = :isDeleted \
            AND NOT EXISTS (SELECT r FROM Reservation r \
                            WHERE r.table = t \
                                AND (:startTime < r.endTime AND :endTime > r.startTime) \
                                AND t.isDeleted = False) \
            ORDER BY t.capacity ASC \
            """)
    List<ReservationTable> findFirstAvailableTableWithLeastCapacity(
            @Param("numberOfGuests") int numberOfGuests,
            @Param("isDeleted") boolean isDeleted,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("""
            SELECT t FROM ReservationTable t \
            WHERE t.capacity >= :numberOfGuests \
                    AND t.status = :status \
            ORDER BY t.capacity ASC \
            """)
    List<ReservationTable> findTableWithLeastCapacity(
            @Param("numberOfGuests") int numberOfGuests,
            @Param("status") TableStatus status
    );

    Optional<ReservationTable> findByTableNumberAndIsDeletedFalse(int tableNumber);


}
