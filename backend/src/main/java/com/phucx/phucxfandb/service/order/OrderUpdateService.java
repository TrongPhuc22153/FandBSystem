package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;

public interface OrderUpdateService {
    OrderDTO updateOrderStatusByEmployee(String username, String orderID, OrderType type, OrderStatus status);
    OrderDTO updateOrderStatusByCustomer(String username, String orderId, OrderType type, OrderStatus status);
    OrderDTO updateOrderStatus(String orderID, OrderType type, OrderStatus status);

    OrderDTO createOrderCustomer(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO createOrderEmployee(String username, RequestOrderDTO requestOrderDTO);
}
