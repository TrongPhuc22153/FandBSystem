package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.enums.PaymentStatus;

public interface OrderUpdateService {
    OrderDTO updateOrderStatusByEmployee(String username, String orderID, OrderType type, OrderStatus status);
    OrderDTO updateOrderStatusByCustomer(String username, String orderId, OrderType type, OrderStatus status);
    OrderDTO updateOrderStatus(String orderID, OrderType type, OrderStatus status);

    OrderDTO updateOrder(String orderId, OrderType type, OrderStatus orderStatus, PaymentStatus paymentStatus);

    OrderDTO updateOrder(String username, String orderId, RequestOrderDTO requestOrderDTO);

    OrderDTO createOrderCustomer(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO createOrderEmployee(String username, RequestOrderDTO requestOrderDTO);
}
