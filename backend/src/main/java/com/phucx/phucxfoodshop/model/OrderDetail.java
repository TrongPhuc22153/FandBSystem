package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import com.phucx.phucxfoodshop.compositeKey.OrderDetailKey;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "orderdetails")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderDetailKey.class)
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "OrderDetail.InsertOrderDetail", procedureName = "InsertOrderDetail",
    parameters = {
        @StoredProcedureParameter(name="productID", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="unitPrice", mode = ParameterMode.IN, type = BigDecimal.class),
        @StoredProcedureParameter(name="quantity", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    }),
    @NamedStoredProcedureQuery(name = "OrderDetail.CreateOrderDetail", procedureName = "CreateOrderDetail",
    parameters = {
        @StoredProcedureParameter(name="productID", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name="orderID", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name="unitPrice", mode = ParameterMode.IN, type = BigDecimal.class),
        @StoredProcedureParameter(name="quantity", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
    }),
})
public class OrderDetail {
    @Id
    private Integer productID;
    @Id
    private String orderID;
    private BigDecimal unitPrice;
    private Integer quantity;
}
