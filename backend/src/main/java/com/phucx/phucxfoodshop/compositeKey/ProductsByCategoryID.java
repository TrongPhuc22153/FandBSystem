package com.phucx.phucxfoodshop.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductsByCategoryID {
    private String categoryName;
    private String productName;    
}
