package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
