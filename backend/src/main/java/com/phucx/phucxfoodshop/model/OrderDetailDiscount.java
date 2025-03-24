package com.phucx.phucxfoodshop.model;

import java.time.LocalDateTime;

import com.phucx.phucxfoodshop.compositeKey.OrderDetailDiscountID;
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

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderDetailDiscountID.class)
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "OrderDetailsDiscounts.InsertOrderDetailDiscount", 
        procedureName = "InsertOrderDetailDiscount", 
        parameters = {
            @StoredProcedureParameter(name="orderID", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="productID", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="discountPercent", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="appliedDate", type = LocalDateTime.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
        }),
    @NamedStoredProcedureQuery(name = "OrderDetailsDiscounts.CreateOrderDetailDiscount", 
        procedureName = "CreateOrderDetailDiscount", 
        parameters = {
            @StoredProcedureParameter(name="orderID", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="productID", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="discountPercent", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="appliedDate", type = LocalDateTime.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
        })
})
@Table(name = "orderdetailsdiscounts")
public class OrderDetailDiscount {
    @Id
    private String orderID;
    @Id
    private Integer productID;
    @Id
    private String discountID;
    private Integer discountPercent;
    private LocalDateTime appliedDate;

}
