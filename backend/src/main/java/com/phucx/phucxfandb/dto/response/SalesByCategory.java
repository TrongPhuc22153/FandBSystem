package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesByCategory{
    Integer productID;
    String productName;
    BigDecimal productSales;
    CategoryDTO category;
}
