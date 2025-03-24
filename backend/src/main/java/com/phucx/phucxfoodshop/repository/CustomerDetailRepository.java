package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import java.util.Optional;
import java.util.List;




@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String>{
    @Procedure(name = "UpdateCustomerInfo")
    Boolean updateCustomerInfo(String customerID, String contactName, 
        String address, String city, String district, String ward, 
        String phone, String picture);

    @Procedure(name = "UpdateAdminCustomerInfo")
    Boolean updateAdminCustomerInfo(String customerID, String contactName, 
        String address, String city, String district, String ward,
        String phone, String picture, Boolean enabled);

    @Procedure(name = "AddNewCustomer")
    public Boolean addNewCustomer(String profileID, String userID, String customerID, String contactName);

    Optional<CustomerDetail> findByUserID(String userID);

    @Query("""
        SELECT c FROM CustomerDetail c WHERE userID IN ?1
            """)
    List<CustomerDetail> findAllByUserID(List<String> userID);
}
