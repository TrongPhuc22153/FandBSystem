package com.phucx.phucxfoodshop.service.discount;

import java.util.List;

import com.phucx.phucxfoodshop.model.dto.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.dto.ResponseFormat;

public interface ValidateDiscountService {
    // validate discount of a specific product
    public ResponseFormat validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts);

}
