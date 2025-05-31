package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {
    @NonNull
    @EntityGraph(attributePaths = {"menuItems", "menuItems.product", "customer", "customer.profile", "customer.profile.user", "employee", "employee.profile", "employee.profile.user", "table"})
    Page<Reservation> findAll(@Nullable Specification<Reservation> spec, @NonNull Pageable pageable);

    Optional<Reservation> findByReservationIdAndCustomerProfileUserUsername(String reservationId, String username);

    Optional<Reservation> findByReservationIdAndEmployeeProfileUserUsername(String reservationId, String username);





}
