package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface OrderReaderService {
    Page<OrderDTO> getOrders(LocalDate date, int pageNumber, int pageSize);
    OrderDTO getOrder(String orderId);
    OrderDTO getOrder(String orderId, OrderType type);
    Order getOrderEntity(String orderId);
    OrderDTO getOrder(String orderId, OrderStatus status);
    Page<OrderDTO> getOrders(OrderStatus status, int pageNumber, int pageSize);
    Page<OrderDTO> getOrders(int pageNumber, int pageSize);

    OrderDTO getOrderByTableId(String tableId);
    // get customer's order
    OrderDTO getOrderByOrderIdAndCustomerUsername(String orderId, String username);
    Page<OrderDTO> getOrdersByCustomerUsername(String username, int pageNumber, int pageSize);
    Page<OrderDTO> getOrdersByCustomerUsername(String username, OrderStatus status, int pageNumber, int pageSize);
    // get employee's order
    OrderDTO getOrderByOrderIdAndEmployeeUsername(String orderId, String username);
    Page<OrderDTO> getOrdersByEmployeeUsername(String username, int pageNumber, int pageSize);
    Page<OrderDTO> getOrdersByEmployeeUsername(String username, OrderStatus status, int pageNumber, int pageSize);
}
