package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity @ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productsizeinfo")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "ProductSizeInfo.CreateProduct",
    procedureName = "CreateProduct", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "quantityPerUnit", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitPrice", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsInStock", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "discontinued", type = Boolean.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "picture", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "description", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "height", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "width", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "length", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "weight", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Boolean.class)
    })
})
public class ProductSizeInfo {
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

    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;
}
