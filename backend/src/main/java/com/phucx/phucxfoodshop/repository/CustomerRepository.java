package com.phucx.phucxfoodshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
}
