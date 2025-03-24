package com.phucx.phucxfoodshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Immutable;

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

@Data
@Entity 
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productdetails")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "ProductDetails.UpdateProduct",
    procedureName = "UpdateProduct", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "quantityPerUnit", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitPrice", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsInStock", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "discontinued", type = Boolean.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "picture", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "description", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Boolean.class)
    }),
    @NamedStoredProcedureQuery(name = "ProductDetails.InsertProduct",
    procedureName = "InsertProduct", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "quantityPerUnit", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitPrice", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsInStock", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "discontinued", type = Boolean.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "picture", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "description", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Boolean.class)
    })
})
public class ProductDetail implements Serializable{
    @Id
    private Integer productID;
    private String productName;
    private Integer categoryID;
    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Boolean discontinued;
    private String picture;
    private String description;
    
    private String categoryName;

    private String discountID;
    private Integer discountPercent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
