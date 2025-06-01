package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, String>, JpaSpecificationExecutor<TableEntity> {

    boolean existsByTableNumber(int tableNumber);

    Page<TableEntity> findByIsDeletedFalse(Pageable pageable);

    Optional<TableEntity> findByTableIdAndIsDeletedFalse(String tableId);

    @Query("""
            SELECT t FROM TableEntity t
            WHERE t.capacity >= :numberOfGuests
              AND t.isDeleted = :isDeleted
              AND NOT EXISTS (
                  SELECT r FROM Reservation r
                  WHERE r.table = t
                    AND r.date = :date
                    AND (:startTime < r.endTime AND :endTime > r.startTime)
              )
            ORDER BY t.capacity ASC
            """)
    List<TableEntity> findFirstAvailableTableWithLeastCapacity(
            @Param("numberOfGuests") int numberOfGuests,
            @Param("isDeleted") boolean isDeleted,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

}
