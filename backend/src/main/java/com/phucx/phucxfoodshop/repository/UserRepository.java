package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true, value =  """
        CALL GetUserRoles(?1)
            """)
    List<String> getUserRoles(String username);

    @Procedure("UpdateUserPassword")
    Boolean updateUserPassword(String userID, String password);

    @Procedure("CreateCustomerUser")
    Boolean createCustomerUser(String userID, String customerID, 
        String profileID, String firstname, String lastname, 
        String email, String username, String password);

    @Procedure("CreateEmployeeUser")
    Boolean createEmployeeUser(String userID, String employeeID, 
        String profileID, String firstname, String lastname, 
        String email, String username, String password);

    @Procedure("UpdateEmailVerification")
    Boolean updateEmailVerification(String username, Boolean status);

    @Query("""
        SELECT u FROM User u JOIN UserRole ur on u.userID=ur.userID \
        WHERE ur.roleName=?1     
        """)
    Optional<User> findByRoleName(String roleName);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u JOIN EmployeeDetail e ON u.userID=e.userID \
        WHERE e.employeeID=?1
            """)
    Optional<User> findByEmployeeID(String employeeID);

    @Query("""
        SELECT u FROM User u JOIN CustomerDetail c ON u.userID=c.userID \
        WHERE c.customerID=?1
            """)
    Optional<User> findByCustomerID(String customerID);

}
