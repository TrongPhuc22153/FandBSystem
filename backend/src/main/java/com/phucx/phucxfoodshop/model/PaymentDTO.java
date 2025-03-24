package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO{
    private String paymentID;
    private Double amount;
    private String orderID;
    private String method;
    private String customerID;
    private String baseUrl;
    public PaymentDTO(Double amount, String orderID, String method, String customerID, String baseUrl) {
        this.amount = amount;
        this.orderID = orderID;
        this.method = method;
        this.customerID = customerID;
        this.baseUrl = baseUrl;
    }
}
