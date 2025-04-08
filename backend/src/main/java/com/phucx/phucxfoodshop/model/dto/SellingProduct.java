package com.phucx.phucxfoodshop.model.dto;

import com.phucx.phucxfoodshop.model.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellingProduct extends Product {
    private Integer quantity;
}
