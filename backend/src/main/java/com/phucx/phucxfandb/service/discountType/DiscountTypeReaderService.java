package com.phucx.phucxfandb.service.discountType;

import com.phucx.phucxfandb.dto.request.DiscountTypeParamsDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.entity.DiscountType;
import org.springframework.data.domain.Page;

public interface DiscountTypeReaderService {
    Page<DiscountTypeDTO> getDiscountTypes(DiscountTypeParamsDTO params);
    DiscountTypeDTO getDiscountType(long id);
    DiscountType getDiscountTypeEntity(long id);
}
