package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {
    @EntityGraph(attributePaths = {"customer", "customer.profile", "customer.profile.user", "employee", "employee.profile", "employee.profile.user", "table"})
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"menuItems", "menuItems.product", "customer", "customer.profile", "customer.profile.user", "employee", "employee.profile", "employee.profile.user", "table"})
    Page<Reservation> findAll(Specification<Reservation> spec, Pageable pageable);





}
