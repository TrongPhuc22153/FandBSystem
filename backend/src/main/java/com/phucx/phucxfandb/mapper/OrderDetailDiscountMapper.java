package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDiscountDTO;
import com.phucx.phucxfandb.entity.Discount;
import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.entity.OrderDetailDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailDiscountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDetail", source = "orderDetail")
    @Mapping(target = "discountPercent", source = "discount.discountPercent")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discount", ignore = true)
    OrderDetailDiscount toOrderDetailsDiscount(RequestOrderDetailsDiscountDTO requestOrderDetailsDiscountDTO, Discount discount, OrderDetail orderDetail);
}
