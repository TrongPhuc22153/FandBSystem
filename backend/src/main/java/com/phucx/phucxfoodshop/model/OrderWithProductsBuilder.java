package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.phucx.phucxfoodshop.constant.OrderStatus;

public class OrderWithProductsBuilder {
    private String orderID;
    private String customerID;
    private String contactName;
    private String employeeID;
    private String salesPerson;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private LocalDateTime shippedDate;
    private List<OrderItem> products = new ArrayList<>(); // Initialize with an empty list
    private Integer shipVia;
    private String shipperName;
    private String shipperPhone;
    private BigDecimal freight = BigDecimal.valueOf(0);
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipDistrict;
    private String shipWard;
    private String phone;
    private BigDecimal totalPrice = BigDecimal.valueOf(0);
    private OrderStatus status;
    private String method;

    public OrderWithProductsBuilder withOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public OrderWithProductsBuilder withCustomerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public OrderWithProductsBuilder withContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public OrderWithProductsBuilder withEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public OrderWithProductsBuilder withSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
        return this;
    }

    public OrderWithProductsBuilder withOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderWithProductsBuilder withRequiredDate(LocalDateTime requiredDate) {
        this.requiredDate = requiredDate;
        return this;
    }

    public OrderWithProductsBuilder withShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
        return this;
    }

    public OrderWithProductsBuilder withProducts(List<OrderItem> products) {
        this.products = products;
        return this;
    }

    public OrderWithProductsBuilder withShipVia(Integer shipVia) {
        this.shipVia = shipVia;
        return this;
    }

    public OrderWithProductsBuilder withShipperName(String shipperName) {
        this.shipperName = shipperName;
        return this;
    }

    public OrderWithProductsBuilder withShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
        return this;
    }

    public OrderWithProductsBuilder withFreight(BigDecimal freight) {
        this.freight = freight;
        return this;
    }

    public OrderWithProductsBuilder withShipName(String shipName) {
        this.shipName = shipName;
        return this;
    }

    public OrderWithProductsBuilder withShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
        return this;
    }

    public OrderWithProductsBuilder withShipCity(String shipCity) {
        this.shipCity = shipCity;
        return this;
    }

    public OrderWithProductsBuilder withShipDistrict(String shipDistrict) {
        this.shipDistrict = shipDistrict;
        return this;
    }

    public OrderWithProductsBuilder withShipWard(String shipWard) {
        this.shipWard = shipWard;
        return this;
    }

    public OrderWithProductsBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public OrderWithProductsBuilder withTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public OrderWithProductsBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderWithProductsBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public OrderWithProducts build() {
        return new OrderWithProducts(
            orderID,
            customerID,
            contactName,
            employeeID,
            salesPerson,
            orderDate,
            requiredDate,
            shippedDate,
            products,
            shipVia,
            shipperName,
            shipperPhone,
            freight,
            shipName,
            shipAddress,
            shipCity,
            shipDistrict,
            shipWard,
            phone,
            totalPrice,
            status,
            method
        );
    }
}
