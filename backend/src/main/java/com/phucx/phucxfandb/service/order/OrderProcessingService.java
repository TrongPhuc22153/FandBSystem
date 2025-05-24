package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderAction;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import org.springframework.security.core.Authentication;

public interface OrderProcessingService {

    OrderDTO cancelOrderByEmployee(String username, String orderId, OrderType type);
    OrderDTO cancelOrderByCustomer(String username, String orderId, OrderType type);
    OrderDTO cancelOrder(String orderId, OrderType type, Authentication authentication);

    OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO);
    OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO);
    PaymentProcessingDTO placeOrder(RequestOrderDTO requestOrderDTO, Authentication authentication);

    OrderDTO completeOrder(String username, String orderId, OrderType type);
    OrderDTO completeTakeAwayOrder(String username, String orderId);
    OrderDTO completeDineInOrder(String username, String orderId);

    OrderDTO markOrderAsPrepared(String username, String orderId, OrderType type);

    OrderDTO preparingOrder(String username, String orderId, OrderType orderType);

    OrderDTO processOrder(Authentication authentication, String orderId, OrderAction action, OrderType type);
}
