package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Named("toPaymentDTO")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "method", source = "method.methodName")
    PaymentDTO toPaymentDTO(Payment payment);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "order.customer", ignore = true)
    @Mapping(target = "order.employee", ignore = true)
    @Mapping(target = "order.waitList.order", ignore = true)
    @Mapping(target = "order.payment", ignore = true)
    @Mapping(target = "order.orderDetails", ignore = true)
    @Mapping(target = "order.shippingAddress", ignore = true)
    @Mapping(target = "reservation.customer", ignore = true)
    @Mapping(target = "reservation.employee", ignore = true)
    @Mapping(target = "reservation.payment", ignore = true)
    @Mapping(target = "reservation.table", ignore = true)
    @Mapping(target = "reservation.menuItems", ignore = true)
    @Mapping(target = "method", source = "method.methodName")
    PaymentDTO toPaymentEntryList(Payment payment);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "order.customer", ignore = true)
    @Mapping(target = "order.employee", ignore = true)
    @Mapping(target = "order.waitList.order", ignore = true)
    @Mapping(target = "order.payment", ignore = true)
    @Mapping(target = "order.shippingAddress", ignore = true)
    @Mapping(target = "reservation.customer", ignore = true)
    @Mapping(target = "reservation.employee", ignore = true)
    @Mapping(target = "reservation.payment", ignore = true)
    @Mapping(target = "method", source = "method.methodName")
    PaymentDTO toPaymentDetail(Payment payment);
}
