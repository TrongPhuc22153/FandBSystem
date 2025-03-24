package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.phucxfoodshop.constant.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
public class OrderWithProducts {
    private String orderID;

    private String customerID;
    private String contactName;

    private String employeeID;
    private String salesPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    private List<OrderItem> products;

    private Integer shipVia;
    private String shipperName;
    private String shipperPhone;

    private BigDecimal freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipDistrict;
    private String shipWard;
    private String phone;
    private BigDecimal totalPrice;
    private OrderStatus status;

    private String method;



    public OrderWithProducts(String orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, BigDecimal freight, String shipName,
            String shipAddress, String shipCity, String phone, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.status = status;
    }


    public OrderWithProducts(String orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, String shipName, String shipAddress,
            String shipCity, String phone, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.status = status;
    }


    public OrderWithProducts(String orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            Integer shipVia, String shipperName, String shipperPhone, BigDecimal freight, String shipName,
            String shipAddress, String shipCity, String phone, BigDecimal totalPrice, OrderStatus status) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public OrderWithProducts(String orderID, String customerID, String contactName, String employeeID,
            String salesPerson, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
            List<OrderItem> products, Integer shipVia, String shipperName, String shipperPhone, BigDecimal freight,
            String shipName, String shipAddress, String shipCity, String phone, BigDecimal totalPrice, OrderStatus status) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.contactName = contactName;
        this.employeeID = employeeID;
        this.salesPerson = salesPerson;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.products = products;
        this.shipVia = shipVia;
        this.shipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public OrderWithProducts() {
        this.products = new ArrayList<>();
        this.totalPrice=BigDecimal.valueOf(0);
        this.freight = BigDecimal.valueOf(0);
    }
}
