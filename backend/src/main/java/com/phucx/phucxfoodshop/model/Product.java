package com.phucx.phucxfoodshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "products")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Product.UpdateProductUnitsInStock", procedureName = "UpdateProductUnitsInStock",
        parameters = {
            @StoredProcedureParameter(name="productID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="unitsInStock", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "Product.UpdateProductsUnitsInStock", procedureName = "UpdateProductsUnitsInStock",
        parameters = {
            @StoredProcedureParameter(name="productIDs", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="unitsInStocks", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        })
})
public class Product implements Serializable{
    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    private Integer productID;

    @Column(name = "ProductName", length = 40, nullable = false)
    private String productName;

    private Integer categoryID;

    @Column(name = "QuantityPerUnit", length = 20)
    private String quantityPerUnit;

    @Column(name = "UnitPrice")
    private BigDecimal unitPrice;

    @Column(name = "UnitsInStock")
    private Integer unitsInStock;

    @Column(name = "Discontinued", nullable = false)
    private Boolean discontinued;

    private String picture;

    private String description;
}
