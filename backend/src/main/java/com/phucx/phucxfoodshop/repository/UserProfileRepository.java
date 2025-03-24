package com.phucx.phucxfoodshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.model.UserProfile;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String>{
    @Query("""
        SELECT u FROM UserProfile u JOIN Customer c ON u.profileID=c.profileID \
        WHERE c.customerID=?1
            """)
    Optional<UserProfile> findByCustomerID(String customerID);

    Optional<UserProfile> findByUserID(String userID);

    @Query("""
        SELECT u FROM UserProfile u JOIN Employee e ON u.profileID=e.profileID \
        WHERE e.employeeID=?1
            """)
    Optional<UserProfile> findByEmployeeID(String employeeID);
}
