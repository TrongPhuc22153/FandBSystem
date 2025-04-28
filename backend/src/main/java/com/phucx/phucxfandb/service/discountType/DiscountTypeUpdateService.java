package com.phucx.phucxfandb.service.discountType;

import com.phucx.phucxfandb.dto.request.RequestDiscountTypeDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;

import java.util.List;

public interface DiscountTypeUpdateService {
    // update
    DiscountTypeDTO updateDiscountType(Long typeId, RequestDiscountTypeDTO requestDiscountTypeDTO);
    // create
    DiscountTypeDTO createDiscountType(RequestDiscountTypeDTO createDiscountTypeDTO);
    List<DiscountTypeDTO> createDiscountTypes(List<RequestDiscountTypeDTO> createDiscountTypeDTOs);
}
