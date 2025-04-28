package com.phucx.phucxfandb.service.discount;

import com.phucx.phucxfandb.dto.request.RequestDiscountDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;

public interface DiscountUpdateService {
    // insert discount
    DiscountDTO createDiscount(RequestDiscountDTO requestDiscountDTO);
    // update r
    DiscountDTO updateDiscount(String discountId, RequestDiscountDTO requestDiscountDTO);
}
