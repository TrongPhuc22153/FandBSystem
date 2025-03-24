package com.phucx.phucxfoodshop.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.model.EmployeeDetail;

@Repository
public interface EmployeeDetailRepostiory extends JpaRepository<EmployeeDetail, String> {
    @Procedure(name = "UpdateEmployeeInfo")
    Boolean updateEmployeeInfo(String employeeID, LocalDate birthDate, 
        String address, String city, String district, String ward, 
        String phone, String picture);

    @Procedure(name = "UpdateAdminEmployeeInfo")
    Boolean updateAdminEmployeeInfo(String employeeID, LocalDate hireDate, 
        LocalDate birthDate, String address, String city, String district, 
        String ward, String phone, String title, String picture, String notes, 
        Boolean enabled);

    @Procedure(name = "AddNewEmployee")
    public Boolean addNewEmployee(String profileID, String userID, String employeeID);

    Optional<EmployeeDetail> findByUserID(String userID);

    @Query("""
        SELECT e FROM EmployeeDetail e WHERE userID in ?1
            """)
    List<EmployeeDetail> findAllByUserID(List<String> userIds);
}
