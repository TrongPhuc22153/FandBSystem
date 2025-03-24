package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.phucx.phucxfoodshop.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Order.InsertOrder", procedureName = "InsertOrder",
        parameters = {
            @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="orderDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="requiredDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="shippedDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="freight", mode = ParameterMode.IN, type = BigDecimal.class),
            @StoredProcedureParameter(name="shipName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipAddress", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipCity", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipperID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
        @NamedStoredProcedureQuery(name = "Order.CreateOrder", procedureName = "CreateOrder",
        parameters = {
            @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="orderDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="requiredDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="shippedDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="freight", mode = ParameterMode.IN, type = BigDecimal.class),
            @StoredProcedureParameter(name="shipName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipAddress", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipCity", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipDistrict", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipWard", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="customerID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="shipperID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
        @NamedStoredProcedureQuery(name = "Order.UpdateOrderStatus", procedureName = "UpdateOrderStatus",
        parameters = {
            @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
        @NamedStoredProcedureQuery(name = "Order.UpdateOrderEmployeeID", procedureName = "UpdateOrderEmployeeID",
        parameters = {
            @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
})
public class Order {
    @Id
    private String orderID;

    private String customerID;

    private String employeeID;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "RequiredDate")
    private LocalDateTime requiredDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ShippedDate")
    private LocalDateTime shippedDate;

    private Integer shipVia;

    private BigDecimal freight;

    @Column(name = "ShipName", length = 40)
    private String shipName;

    @Column(name = "ShipAddress", length = 200)
    private String shipAddress;

    @Column(name = "ShipCity", length = 50)
    private String shipCity;

    @Column(name = "ShipDistrict", length = 50)
    private String shipDistrict;

    @Column(name = "ShipWard", length = 50)
    private String shipWard;

    private String phone;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(String customerID, String employeeID, LocalDateTime orderDate, LocalDateTime requiredDate,
            LocalDateTime shippedDate, Integer shipVia, BigDecimal freight, String shipName, String shipAddress,
            String shipCity, String shipDistrict, String shipWard, String phone, OrderStatus status) {
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipVia = shipVia;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipDistrict = shipDistrict;
        this.shipWard = shipWard;
        this.phone = phone;
        this.status = status;
    }

}
