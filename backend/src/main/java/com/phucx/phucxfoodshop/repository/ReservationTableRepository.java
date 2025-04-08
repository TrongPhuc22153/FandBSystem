package com.phucx.phucxfoodshop.repository;

import com.phucx.phucxfoodshop.model.entity.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, String> {
}
