package com.phucx.phucxfandb.service.discount;

import com.phucx.phucxfandb.dto.request.ValidateProductDiscountsDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;

public interface ValidateDiscountService {
    // validate discount of a specific product
    ResponseDTO<Boolean> validateDiscountsOfProduct(ValidateProductDiscountsDTO validateProductDiscountsDTO);

}
