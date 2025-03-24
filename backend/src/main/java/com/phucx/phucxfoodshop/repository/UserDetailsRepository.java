package com.phucx.phucxfoodshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.compositeKey.UserRoleID;
import com.phucx.phucxfoodshop.model.UserDetails;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, UserRoleID> {
    Page<UserDetails> findByRoleName(String roleName, Pageable pageable);
    
    @Query("""
        SELECT u FROM UserDetails u \
        WHERE u.roleName=?2 AND \
              u.username LIKE ?1
        """)
    Page<UserDetails> findByUsernameLikeAndRolename(String username, String roleName, Pageable pageable);


    @Query("""
        SELECT u FROM UserDetails u \
        WHERE u.roleName=?2 AND \
              u.firstName LIKE ?1
        """)
    Page<UserDetails> findByFirstNameLikeAndRolename(String firstName, String roleName, Pageable pageable);

    @Query("""
        SELECT u FROM UserDetails u \
        WHERE u.roleName=?2 AND \
              u.lastName LIKE ?1
        """)
    Page<UserDetails> findByLastNameLikeAndRolename(String lastName, String roleName, Pageable pageable);

    @Query("""
        SELECT u FROM UserDetails u \
        WHERE u.roleName=?2 AND \
              u.email LIKE ?1
        """)
    Page<UserDetails> findByEmailLikeAndRolename(String email, String roleName, Pageable pageable);
}
