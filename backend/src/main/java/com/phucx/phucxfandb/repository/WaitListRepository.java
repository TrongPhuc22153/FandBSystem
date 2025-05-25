package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.WaitListStatus;
import com.phucx.phucxfandb.entity.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitListRepository extends JpaRepository<WaitList, String>, JpaSpecificationExecutor<WaitList> {

    @Query("""
        SELECT COUNT(w) > 0 \
        FROM WaitList w \
        WHERE w.table.tableId = :tableId \
            AND w.status = :status \
            AND w.table.isDeleted = false \
        """)
    boolean existsByTableIdAndWaitListStatus(
            @Param("tableId") String tableId,
            @Param("status") WaitListStatus status);
}
