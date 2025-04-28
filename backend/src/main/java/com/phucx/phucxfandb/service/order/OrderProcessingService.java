package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;

public interface OrderProcessingService {
    //
    OrderDTO confirmOrder(String username, String orderId, OrderType type);
    OrderDTO fulfillOrder(String username, String orderID, OrderType type);
    void cancelPendingOrder(String username, String orderId, OrderType type);
    void cancelOrderByEmployee(String username, String orderId, OrderType type);
    void cancelConfirmedOrder(String username, String orderId, OrderType type);
    OrderDTO receiveCustomerOrder(String  username, String orderId, OrderType type);
    OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO completeOrder(String tableId, String orderId, OrderType type);
    OrderDTO completeTakeAwayOrder(String username, String orderId, OrderType type);

    OrderDTO markOrderAsPrepared(String orderId);
}
