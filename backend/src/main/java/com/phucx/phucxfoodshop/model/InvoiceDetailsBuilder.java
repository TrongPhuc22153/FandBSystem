package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.phucx.phucxfoodshop.constant.OrderStatus;

public class InvoiceDetailsBuilder {
    private String orderID;
    private List<ProductWithBriefDiscount> products = new ArrayList<>();;
    private String customerID;
    private String employeeID;
    private String salesPerson;

    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipDistrict;
    private String shipWard;
    private String phone;
    
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private String shipperName;
    private LocalDateTime shippedDate;
    private BigDecimal totalPrice = new BigDecimal(0);
    private BigDecimal freight = new BigDecimal(0);
    private OrderStatus status;
    private String paymentMethod;

    public InvoiceDetailsBuilder withOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public InvoiceDetailsBuilder withProducts(List<ProductWithBriefDiscount> products) {
        this.products = products;
        return this;
    }

    public InvoiceDetailsBuilder withCustomerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public InvoiceDetailsBuilder withEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public InvoiceDetailsBuilder withSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
        return this;
    }

    public InvoiceDetailsBuilder withShipName(String shipName) {
        this.shipName = shipName;
        return this;
    }

    public InvoiceDetailsBuilder withShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
        return this;
    }

    public InvoiceDetailsBuilder withShipCity(String shipCity) {
        this.shipCity = shipCity;
        return this;
    }

    public InvoiceDetailsBuilder withShipDistrict(String shipDistrict) {
        this.shipDistrict = shipDistrict;
        return this;
    }

    public InvoiceDetailsBuilder withShipWard(String shipWard) {
        this.shipWard = shipWard;
        return this;
    }

    public InvoiceDetailsBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public InvoiceDetailsBuilder withOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public InvoiceDetailsBuilder withRequiredDate(LocalDateTime requiredDate) {
        this.requiredDate = requiredDate;
        return this;
    }

    public InvoiceDetailsBuilder withShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
        return this;
    }

    public InvoiceDetailsBuilder withShipperName(String shipperName) {
        this.shipperName = shipperName;
        return this;
    }

    public InvoiceDetailsBuilder withTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public InvoiceDetailsBuilder withFreight(BigDecimal freight) {
        this.freight = freight;
        return this;
    }

    public InvoiceDetailsBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public InvoiceDetailsBuilder withPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public InvoiceDetails build() {
        return new InvoiceDetails(
            orderID,
            products,
            customerID,
            employeeID,
            salesPerson,
            shipName,
            shipAddress,
            shipCity,
            shipDistrict,
            shipWard,
            phone,
            orderDate,
            requiredDate,
            shippedDate,
            shipperName,
            totalPrice,
            freight,
            status,
            paymentMethod
        );
    }
}
