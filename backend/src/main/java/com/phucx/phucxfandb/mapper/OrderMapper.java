package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {OrderDetailsMapper.class, UserMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "payment", qualifiedByName = "toPaymentDTO")
    @Mapping(target = "tableOccupancy.order", ignore = true)
    @Mapping(target = "tableOccupancy.reservation", ignore = true)
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    OrderDTO toOrderDTO(Order order);

    @Named("toOrderListEntryDTO")
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "tableOccupancy.order", ignore = true)
    @Mapping(target = "tableOccupancy.reservation", ignore = true)
    @Mapping(target = "orderDetails", qualifiedByName = {"toOrderDetailsDTO"})
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "shippingAddress", ignore = true)
    OrderDTO toOrderListEntryDTO(Order order);

    @Mapping(target = "tableOccupancy", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "payment", ignore = true)
    Order toCustomerOrder(RequestOrderDTO request, Customer customer, ShippingAddress shippingAddress);

    @Mapping(target = "tableOccupancy", source = "tableOccupancy")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "shippingAddress", ignore = true)
    Order toEmployeeOrder(RequestOrderDTO request, Employee employee, TableOccupancy tableOccupancy);
}
