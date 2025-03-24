package com.phucx.phucxfoodshop.repository;

import com.phucx.phucxfoodshop.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, String> {
}
