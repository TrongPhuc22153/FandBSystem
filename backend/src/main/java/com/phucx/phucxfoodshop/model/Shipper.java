package com.phucx.phucxfoodshop.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shippers")
public class Shipper implements Serializable{
    @Id
    @Column(name = "ShipperID", columnDefinition = "ShipperID", nullable = false)
    private Integer shipperID;
    @Column(name = "CompanyName", columnDefinition = "CompanyName", nullable = false, length = 40)
    private String companyName;
    @Column(name = "Phone", length = 24)
    private String phone;
}
