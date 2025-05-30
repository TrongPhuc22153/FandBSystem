package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.PaymentMethodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodTypeRepository extends JpaRepository<PaymentMethodType, Long> {
}
