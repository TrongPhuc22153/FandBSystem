package com.phucx.phucxfoodshop.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{
    
    @Procedure("SavePayment")
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, 
        Double amount, String customerID, String orderID, String status, String paymentMethod);

    @Procedure("SaveFullPayment")
    public Boolean saveFullPayment(String paymentID, LocalDateTime paymentDate, 
        Double amount, String transactionID, String customerID, String orderID, 
        String status, String paymentMethod);

    @Procedure("UpdatePayment")
    public Boolean updatePayment(String paymentID, String transactionID, String status);

    @Procedure("UpdatePaymentStatus")
    public Boolean updatePaymentStatus(String paymentID, String status);

    @Procedure("UpdatePaymentStatusByOrderID")
    public Boolean UpdatePaymentStatusByOrderID(String orderID, String status);

    @Query("""
        SELECT p FROM Payment p \
        WHERE p.orderID=?1
            """)
    public Optional<Payment> findByOrderID(String orderID);
}
