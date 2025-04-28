package com.phucx.phucxfandb.service.discountType;

import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.entity.DiscountType;
import org.springframework.data.domain.Page;

public interface DiscountTypeReaderService {
    Page<DiscountTypeDTO> getDiscountTypes(int pageNumber, int pageSize);
    DiscountTypeDTO getDiscountType(long categoryID);
    DiscountTypeDTO getDiscountType(String categoryName);
    DiscountType getDiscountTypeEntity(long categoryId);
}
