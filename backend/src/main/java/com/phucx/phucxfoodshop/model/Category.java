package com.phucx.phucxfoodshop.model;

import java.io.Serializable;

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

@Data @Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@NamedStoredProcedureQueries(
    @NamedStoredProcedureQuery(name = "Category.AddCategory", procedureName = "AddCategory",
    parameters = {
        @StoredProcedureParameter(name="categoryname", type = String.class),
        @StoredProcedureParameter(name="description", type = String.class),
        @StoredProcedureParameter(name="picture", type = String.class),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    })
)
public class Category implements Serializable{
    @Id
    private Integer categoryID;
    private String categoryName;
    private String description;
    private String picture;
}
