package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailsMapper.class, UserMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "payment", qualifiedByName = "toPaymentDTO")
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    OrderDTO toOrderDTO(Order order);

    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "orderDetails", qualifiedByName = {"toOrderDetailsDTO"})
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "shippingAddress", ignore = true)
    OrderDTO toOrderListEntryDTO(Order order);

    @Mapping(target = "table", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "payment", ignore = true)
    Order toCustomerOrder(RequestOrderDTO order, Customer customer, ShippingAddress shippingAddress);

    @Mapping(target = "table", source = "table")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Order toEmployeeOrder(RequestOrderDTO orderDTO, Employee employee, ReservationTable table);
}
