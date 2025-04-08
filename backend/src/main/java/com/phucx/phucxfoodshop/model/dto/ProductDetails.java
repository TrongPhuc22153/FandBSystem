package com.phucx.phucxfoodshop.model.dto;

import com.phucx.phucxfoodshop.model.entity.ProductDetail;
import com.phucx.phucxfoodshop.model.entity.ProductSize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {
    private ProductDetail product;
    private ProductSize size;
}
