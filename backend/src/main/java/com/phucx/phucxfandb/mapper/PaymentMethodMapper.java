package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestPaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PaymentMethod toPaymentMethod(RequestPaymentMethodDTO requestPaymentMethodDTO);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updatePaymentMethodFromDTO(RequestPaymentMethodDTO requestPaymentMethodDTO, @MappingTarget PaymentMethod paymentMethod);
}
