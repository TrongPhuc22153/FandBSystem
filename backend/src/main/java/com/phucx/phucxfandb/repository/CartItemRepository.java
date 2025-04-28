package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    
}
