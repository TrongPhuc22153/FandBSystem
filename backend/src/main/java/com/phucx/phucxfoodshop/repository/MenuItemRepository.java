package com.phucx.phucxfoodshop.repository;

import com.phucx.phucxfoodshop.compositeKey.MenuItemKey;
import com.phucx.phucxfoodshop.model.entity.MenuItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, MenuItemKey> {
}
