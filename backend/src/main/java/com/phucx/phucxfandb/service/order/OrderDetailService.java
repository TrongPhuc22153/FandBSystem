package com.phucx.phucxfandb.service.order;

import com.phucx.phucxfandb.constant.OrderItemStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;

public interface OrderDetailService {
    OrderDTO updateOrderItemQuantity(String orderId, String orderItemId, RequestOrderDetailsDTO request);

    OrderDTO addOrderItem(String orderId, RequestOrderDetailsDTO request);

    void updateOrderItemStatus(String orderId, OrderItemStatus status);

    void updateOrderItemStatus(String orderId, OrderItemStatus originalStatus, OrderItemStatus statusToUpdate);

}
