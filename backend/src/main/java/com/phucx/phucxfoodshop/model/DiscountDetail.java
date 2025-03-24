package com.phucx.phucxfoodshop.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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

@Entity
@Immutable
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "discountdetails")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "DiscountDetail.UpdateDiscount", procedureName = "UpdateDiscount",
    parameters = {
        @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountPercent", type = Integer.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountCode", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="startDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="endDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="active", type = Boolean.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountType", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "DiscountDetail.InsertDiscount", procedureName = "InsertDiscount",
    parameters = {
        @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountPercent", type = Integer.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountCode", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="startDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="endDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="active", type = Boolean.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountType", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="productID", type = Integer.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "DiscountDetail.UpdateDiscountStatus", procedureName = "UpdateDiscountStatus",
    parameters = {
        @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="active", type = Boolean.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    })
})
public class DiscountDetail implements Serializable{
    @Id
    private String discountID;
    private Integer discountPercent;
    private String discountCode;
    private String discountType;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private Boolean active;
}
