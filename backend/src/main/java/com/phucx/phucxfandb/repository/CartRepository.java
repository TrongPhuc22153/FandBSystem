package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"cartItems"})
    Optional<Cart> findByCustomerProfileUserUsername(String username);

    @Transactional(readOnly = true)
    boolean existsByCustomerProfileUserUsername(String username);
}
