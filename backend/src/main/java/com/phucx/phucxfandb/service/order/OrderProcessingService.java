package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderAction;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import org.springframework.security.core.Authentication;

public interface OrderProcessingService {
    //
    OrderDTO confirmOrder(String username, String orderId, OrderType type);
    OrderDTO fulfillOrder(String username, String orderID, OrderType type);
    OrderDTO cancelPendingOrder(String username, String orderId, OrderType type);
    OrderDTO cancelOrderByEmployee(String username, String orderId, OrderType type);
    OrderDTO cancelConfirmedOrder(String username, String orderId, OrderType type);
    OrderDTO receiveCustomerOrder(String  username, String orderId, OrderType type);
    OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO completeOrder(String username, String orderId, OrderType type);
    OrderDTO completeTakeAwayOrder(String username, String orderId);
    OrderDTO completeDineInOrder(String username, String orderId);

    OrderDTO markOrderAsPrepared(String orderId);

    OrderDTO preparingOrder(String username, String orderId, OrderType orderType);

    OrderDTO placeOrder(RequestOrderDTO requestOrderDTO, Authentication authentication);

    OrderDTO processOrder(String username, String orderId, OrderAction action, OrderType type);
}
