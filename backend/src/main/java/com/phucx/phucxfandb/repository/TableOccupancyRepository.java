package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TableOccupancyRepository extends JpaRepository<TableOccupancy, String>, JpaSpecificationExecutor<TableOccupancy> {

    @Query("""
        SELECT COUNT(w) > 0 \
        FROM TableOccupancy w \
        WHERE w.table.tableId = :tableId \
            AND w.status = :status \
            AND w.table.isDeleted = false \
        """)
    boolean existsByTableIdAndTableOccupancyStatus(
            @Param("tableId") String tableId,
            @Param("status") TableOccupancyStatus status);

    @Query("""
            SELECT o
            FROM TableOccupancy o
            WHERE o.table.tableId = :tableId
                AND o.date = :date
                AND o.startTime <= :currentTime
                AND (o.endTime IS NULL OR o.endTime > :currentTime)
                AND o.status IN ('SEATED', 'CLEANING')
            """)
    List<TableOccupancy> findActiveOccupancies(
            @Param("tableId") String tableId,
            @Param("date") LocalDate date,
            @Param("currentTime") LocalTime currentTime);

    @Query("""
            SELECT o
            FROM TableOccupancy o
            WHERE o.table.tableId IN :tableIds
                AND o.date = :date
                AND o.startTime <= :currentTime
                AND (o.endTime IS NULL OR o.endTime > :currentTime)
                AND o.status IN ('SEATED', 'CLEANING')
            """)
    List<TableOccupancy> findActiveOccupancies(
            @Param("tableIds") Collection<String> tableIds,
            @Param("date") LocalDate date,
            @Param("currentTime") LocalTime currentTime);

    @Query("""
            SELECT o
            FROM TableOccupancy o
            WHERE o.date = :date
                AND o.endTime IS NULL
                AND o.table.tableId IN :tableIds
                AND o.status IN ('SEATED', 'CLEANING')
            """)
    List<TableOccupancy> findActiveOccupancies(
            @Param("date") LocalDate date,
            @Param("tableIds") Collection<String> tableIds);
}
