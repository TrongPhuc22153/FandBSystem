package com.phucx.phucxfoodshop.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer implements Serializable{
    @Id
    @Column(name = "CustomerID", length = 36, nullable = false)
    private String customerID;
    @Column(name = "ContactName", length = 30)
    private String contactName;
    
    private String profileID;
}
