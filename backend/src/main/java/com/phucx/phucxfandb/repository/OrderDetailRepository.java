package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>{

}
