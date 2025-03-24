package com.phucx.phucxfoodshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.PaymentMethod;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String>{
    @Query("""
        SELECT m FROM Payment p JOIN PaymentMethod m on p.methodID=m.methodID \
        WHERE p.paymentID=?1
            """)
    Optional<PaymentMethod> findByPaymentID(String paymentID);

    @Query("""
        SELECT m FROM Payment p JOIN PaymentMethod m on p.methodID=m.methodID \
        WHERE p.orderID=?1
            """)
    Optional<PaymentMethod> findByOrderID(String orderID);
}
