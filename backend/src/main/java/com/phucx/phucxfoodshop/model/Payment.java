package com.phucx.phucxfoodshop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Payment.saveFullPayment", procedureName = "SaveFullPayment",
    parameters = {
        @StoredProcedureParameter(name="paymentID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="paymentDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="amount", type = Double.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="transactionID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="customerID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="orderID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="paymentMethod", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "Payment.savePayment", procedureName = "SavePayment",
    parameters = {
        @StoredProcedureParameter(name="paymentID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="paymentDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="amount", type = Double.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="customerID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="orderID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="paymentMethod", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "Payment.updatePayment", procedureName = "UpdatePayment",
    parameters = {
        @StoredProcedureParameter(name="paymentID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="transactionID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "Payment.updatePaymentStatus", procedureName = "UpdatePaymentStatus",
    parameters = {
        @StoredProcedureParameter(name="paymentID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "Payment.updatePaymentStatusByOrderID", procedureName = "UpdatePaymentStatusByOrderID",
    parameters = {
        @StoredProcedureParameter(name="orderID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
})
public class Payment {
    @Id
    private String paymentID;
    private LocalDateTime paymentDate;
    private String transactionID;
    private Double amount;
    private String status;
    private String customerID;
    private String orderID;
    private String methodID;

    public Payment(String paymentID, LocalDateTime paymentDate, Double amount, String status, String customerID,
            String orderID) {
        this.paymentID = paymentID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
        this.customerID = customerID;
        this.orderID = orderID;
    }
}
