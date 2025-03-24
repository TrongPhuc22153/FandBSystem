package com.phucx.phucxfoodshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.compositeKey.OrderDetailDiscountID;
import com.phucx.phucxfoodshop.model.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, OrderDetailDiscountID> {
    @Procedure("GetCustomerInvoice")
    List<Invoice> getCustomerInvoice(String orderId, String customerId);
}
