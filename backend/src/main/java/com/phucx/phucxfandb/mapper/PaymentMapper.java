package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "customer", ignore = true)
    PaymentDTO toPaymentDTO(Payment payment);

    @Mapping(target = "method", source = "paymentMethod")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toPayment(RequestPaymentDTO requestPaymentDTO, PaymentMethod paymentMethod, Customer customer);
}
