package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, String>, JpaSpecificationExecutor<TableEntity> {

    boolean existsByTableNumber(int tableNumber);

    List<TableEntity> findByIsDeletedFalse();

    Optional<TableEntity> findByTableIdAndIsDeletedFalse(String tableId);
}
