package com.phucx.phucxfandb.repository;


import com.phucx.phucxfandb.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    @EntityGraph(attributePaths = {"profile"})
    Optional<Customer> findByCustomerId(String customerId);

    @EntityGraph(attributePaths = {"profile"})
    Optional<Customer> findByProfileUserUsername(String username);
}
