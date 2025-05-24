package com.phucx.phucxfandb.service.discount;

import com.phucx.phucxfandb.dto.request.DiscountRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;
import org.springframework.data.domain.Page;

public interface DiscountReaderService {
    Page<DiscountDTO> getDiscountsByProductId(long productId, DiscountRequestParamsDTO params);
    DiscountDTO getDiscount(String discountID);
} 
