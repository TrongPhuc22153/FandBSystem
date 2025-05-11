package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderDetailsMapper.class})
public interface OrderMapper {

    @Mapping(target = "customer.profile.user.roles", ignore = true)
    @Mapping(target = "employee.profile.user.roles", ignore = true)
    OrderDTO toOrderDTO(Order order);

    @Mapping(target = "orderDetails", qualifiedByName = {"toOrderDetailsDTO"})
    @Mapping(target = "employee.profile.user.roles", ignore = true)
    @Mapping(target = "customer.profile.user.roles", ignore = true)
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
    Order toCustomerOrder(RequestOrderDTO order, Customer customer);

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
    Order toEmployeeOrder(RequestOrderDTO orderDTO, Employee employee, ReservationTable table);

    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "table", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    void updateOrder(RequestOrderDTO requestOrderDTO, @MappingTarget Order order);

}
