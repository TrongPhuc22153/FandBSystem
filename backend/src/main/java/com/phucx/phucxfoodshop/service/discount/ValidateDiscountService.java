package com.phucx.phucxfoodshop.service.discount;

import java.util.List;

import com.phucx.phucxfoodshop.model.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.ResponseFormat;

public interface ValidateDiscountService {
    // validate discount of a specific product
    public ResponseFormat validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts);

}
