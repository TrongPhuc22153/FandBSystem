package com.phucx.phucxfandb.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
public class SalesByCategory{
    Integer productID;
    String productName;
    BigDecimal productSales;
    CategoryDTO category;
}
