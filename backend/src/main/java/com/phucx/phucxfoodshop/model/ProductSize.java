package com.phucx.phucxfoodshop.model;

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

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productsize")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "productsize.CreateProductSize", 
        procedureName = "CreateProductSize", parameters = {
            @StoredProcedureParameter(name = "productsizeid", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name = "productid", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "height", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "width", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "length", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "weight", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "productsize.UpdateProductSize", 
        procedureName = "UpdateProductSize", parameters = {
            @StoredProcedureParameter(name = "productID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "height", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "width", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "length", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "weight", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
})
public class ProductSize {
    @Id
    private String id;
    private Integer productID;
    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;
    
    public ProductSize(Integer productID, Integer height, Integer length, Integer weight, Integer width) {
        this.productID = productID;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.width = width;
    }
}
