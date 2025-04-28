package com.phucx.phucxfandb.service.discount;

import com.phucx.phucxfandb.dto.response.DiscountDTO;
import org.springframework.data.domain.Page;

public interface DiscountReaderService {
    // get discount
    Page<DiscountDTO> getDiscounts(int pageNumber, int pageSize);
    Page<DiscountDTO> getDiscountsByProductId(long productId, int pageNumber, int pageSize);
    DiscountDTO getDiscount(String discountID);
} 
