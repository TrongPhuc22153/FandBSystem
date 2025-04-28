package com.phucx.phucxfandb.service.order.imp;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {
    private final OrderUpdateService orderUpdateService;
    private final OrderReaderService orderReaderService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final ReservationTableUpdateService reservationTableUpdateService;

    @Override
    public OrderDTO confirmOrder(String username, String orderId, OrderType type) {
        log.info("confirmOrder(username={}, orderId={}, type={})", username, orderId, type);
        OrderDTO orderDTO = orderUpdateService.updateOrderStatusAndEmployee(
                username, orderId, type, OrderStatus.CONFIRMED);

        RequestNotificationDTO requestNotificationDTO = RequestNotificationDTO.builder()
                .title(NotificationTitle.PLACE_ORDER.getValue())
                .message("Your order has been confirmed")
                .topic(NotificationTopic.ORDER)
                .senderUsername(username)
                .receiverUsername(orderDTO.getCustomer().getProfile().getUser().getUsername())
                .build();
        sendOrderNotificationService.sendOrderNotificationToCustomer(orderId, requestNotificationDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO fulfillOrder(String username, String orderId, OrderType type) {
        log.info("fulfillOrder(username={}, orderId={}, type={})", username, orderId, type);
        OrderDTO orderDTO = orderUpdateService.updateOrderStatusByEmployee(
                username, orderId, type, OrderStatus.CONFIRMED);

        RequestNotificationDTO requestNotificationDTO = RequestNotificationDTO.builder()
                .title(NotificationTitle.PLACE_ORDER.getValue())
                .message("Your order has been fulfilled")
                .topic(NotificationTopic.ORDER)
                .senderUsername(username)
                .receiverUsername(orderDTO.getCustomer().getProfile().getUser().getUsername())
                .build();
        sendOrderNotificationService.sendOrderNotificationToCustomer(orderId, requestNotificationDTO);
        return orderDTO;
    }

    @Override
    public void cancelPendingOrder(String username, String orderId, OrderType type) {
        log.info("cancelPendingOrder(username={}, orderId={}, type={})", username, orderId, type);
        orderUpdateService.updateOrderStatusAndEmployee(username, orderId, type, OrderStatus.CANCELLED);
    }

    @Override
    public void cancelOrderByEmployee(String username, String orderId, OrderType type) {
        log.info("cancelOrderByEmployee(username={}, orderId={}, type={})", username, orderId, type);
        orderUpdateService.updateOrderStatusByEmployee(username, orderId, type, OrderStatus.CANCELLED);
    }

    @Override
    public void cancelConfirmedOrder(String username, String orderId, OrderType type) {
        log.info("cancelConfirmedOrder(username={}, orderId={}, type={})", username, orderId, type);
        orderUpdateService.updateOrderStatusByCustomer(username, orderId, type, OrderStatus.CANCELLED);
    }

    @Override
    public OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO) {
        log.info("placeOrderByCustomer(username={}, requestOrderDTO={})", username, requestOrderDTO);
        OrderDTO newOrderDTO = orderUpdateService.createOrderCustomer(username, requestOrderDTO);

        RequestNotificationDTO requestNotificationDTO = RequestNotificationDTO.builder()
                .title(NotificationTitle.PLACE_ORDER.getValue())
                .message(String.format("Order of user %s has been placed", username))
                .topic(NotificationTopic.ORDER)
                .senderUsername(username)
                .build();
        sendOrderNotificationService.sendOrderNotificationToTopic(newOrderDTO.getOrderId(), requestNotificationDTO);
        return newOrderDTO;
    }

    @Override
    public OrderDTO receiveCustomerOrder(String username, String orderId, OrderType type) {
        log.info("receiveCustomerOrder(username={}, orderId={}, type={})", username, orderId, type);
        return orderUpdateService.updateOrderStatusByCustomer(
                username, orderId, type, OrderStatus.COMPLETED);
    }

    @Override
    public OrderDTO markOrderAsPrepared(String orderId) {
        log.info("placeOrderByUsername(orderId={})", orderId);
        return orderUpdateService.updateOrderStatus(orderId, OrderStatus.PREPARED);
    }

    @Override
    public OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO) {
        log.info("placeOrderByEmployee(username={}, requestOrderDTO={})",
                username, requestOrderDTO);
        return orderUpdateService.createOrderEmployee(username, requestOrderDTO);
    }

    @Override
    public OrderDTO completeOrder(String tableId, String orderId, OrderType type) {
        log.info("completeOrder(tableId={}, orderId={}, type={})", tableId, orderId, type);

        OrderDTO orderDTO = orderReaderService.getOrder(orderId, type);
        if(!orderDTO.getOrderId().equals(orderId)){
            throw new NotFoundException(String.format("Table with id %s and order with id %s not found", tableId, orderId));
        }
        reservationTableUpdateService.updateTableStatus(tableId, TableStatus.UNOCCUPIED);
        return orderUpdateService.updateOrderStatus(orderDTO.getOrderId(), type, OrderStatus.COMPLETED);
    }

    @Override
    public OrderDTO completeTakeAwayOrder(String username, String orderId, OrderType type) {
        log.info("completeTakeAwayOrder(username={}, orderId={}, type={})", username, orderId, type);
        OrderDTO orderDTO = orderReaderService.getOrder(orderId, type);
        return orderUpdateService.updateOrderStatus(orderDTO.getOrderId(), type, OrderStatus.COMPLETED);
    }
}
