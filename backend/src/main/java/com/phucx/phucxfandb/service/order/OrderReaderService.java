package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.dto.request.OrderRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface OrderReaderService {
    OrderDTO getOrder(String orderId);
    OrderDTO getOrder(String orderId, OrderType type);
    Order getOrderEntity(String orderId);
    Order getOrderEntity(String orderId, OrderType type);
    Page<OrderDTO> getOrders(OrderRequestParamsDTO params, Authentication authentication);
}
