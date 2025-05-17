package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.ShippingAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    Page<ShippingAddress> findByIsDeletedFalse(Pageable pageable);

    Optional<ShippingAddress> findByIdAndIsDeletedFalse(long id);

    Optional<ShippingAddress> findByCustomerProfileUserUsernameAndIdAndIsDeletedFalse(String username, long id);

    List<ShippingAddress> findByCustomerProfileUserUsernameAndIsDeletedFalse(String username);
    
    boolean existsByCustomerCustomerIdAndIsDeletedFalse(String customerId);
}
