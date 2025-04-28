package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
    
    @EntityGraph(attributePaths = {"profile"})
    Optional<Employee> findByEmployeeIdAndIsDeletedFalse(String employeeId);
    
    @EntityGraph(attributePaths = {"profile"})
    Optional<Employee> findByProfileUserUserIdAndIsDeletedFalse(String userId);
    
    @EntityGraph(attributePaths = {"profile"})
    Optional<Employee> findByProfileUserUsernameAndIsDeletedFalse(String username);
    
    Page<Employee> findByIsDeletedFalse(Pageable pageable);
    
}
