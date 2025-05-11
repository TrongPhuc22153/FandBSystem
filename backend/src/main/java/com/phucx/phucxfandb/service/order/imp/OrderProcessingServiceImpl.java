package com.phucx.phucxfandb.service.order.imp;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public OrderDTO cancelPendingOrder(String username, String orderId, OrderType type) {
        log.info("cancelPendingOrder(username={}, orderId={}, type={})", username, orderId, type);
        return orderUpdateService.updateOrderStatusAndEmployee(username, orderId, type, OrderStatus.CANCELLED);
    }

    @Override
    public OrderDTO cancelOrderByEmployee(String username, String orderId, OrderType type) {
        log.info("cancelOrderByEmployee(username={}, orderId={}, type={})", username, orderId, type);
        return orderUpdateService.updateOrderStatusByEmployee(username, orderId, type, OrderStatus.CANCELLED);
    }

    @Override
    public OrderDTO cancelConfirmedOrder(String username, String orderId, OrderType type) {
        log.info("cancelConfirmedOrder(username={}, orderId={}, type={})", username, orderId, type);
        return orderUpdateService.updateOrderStatusByCustomer(username, orderId, type, OrderStatus.CANCELLED);
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
        log.info("markOrderAsPrepared(orderId={})", orderId);
        return orderUpdateService.updateOrderStatus(orderId, OrderStatus.PREPARED);
    }

    @Override
    public OrderDTO preparingOrder(String username, String orderId, OrderType type) {
        log.info("preparingOrder(username={}, orderId={}, type={})", username, orderId, type);
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.PREPARING);
    }

    @Override
    public OrderDTO placeOrder(RequestOrderDTO requestOrderDTO, Authentication authentication) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        if(roleNames.contains(RoleName.CUSTOMER) && requestOrderDTO.getType().equals(OrderType.TAKE_AWAY)){
            return this.placeOrderByCustomer(authentication.getName(), requestOrderDTO);
        }else if(roleNames.contains(RoleName.EMPLOYEE) && requestOrderDTO.getType().equals(OrderType.DINE_IN)){
            return this.placeOrderByEmployee(authentication.getName(), requestOrderDTO);
        }else{
            throw new IllegalArgumentException("Invalid order type");
        }
    }

    @Override
    public OrderDTO processOrder(String username, String orderId, OrderAction action, OrderType type) {
        return switch (action){
            case PREPARING -> this.preparingOrder(username, orderId, type);
            case READY -> this.markOrderAsPrepared(orderId);
            case COMPLETE -> this.completeOrder(username, orderId, type);
            case CANCEL -> this.cancelOrderByEmployee(username, orderId, type);
        };
    }

    @Override
    public OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO) {
        log.info("placeOrderByEmployee(username={}, requestOrderDTO={})",
                username, requestOrderDTO);
        return orderUpdateService.createOrderEmployee(username, requestOrderDTO);
    }

    @Override
    public OrderDTO completeOrder(String username, String orderId, OrderType type) {
        log.info("completeOrder(orderId={}, type={})", orderId, type);
        return switch (type){
            case DINE_IN -> this.completeDineInOrder(username, orderId);
            case TAKE_AWAY -> this.completeTakeAwayOrder(username, orderId);
        };
    }

    @Override
    public OrderDTO completeDineInOrder(String username, String orderId){
        log.info("completeDineInOrder(username={}, orderId={})", username, orderId);
        Order order = orderReaderService.getOrderEntity(orderId, OrderType.DINE_IN);
        if(!order.getOrderId().equals(orderId)){
            throw new NotFoundException(String.format("Table with id %s and order with id %s not found", order.getTable().getTableId(), orderId));
        }
        reservationTableUpdateService.updateTableStatus(order.getTable().getTableId(), TableStatus.UNOCCUPIED);
        return orderUpdateService.updateOrderStatus(order.getOrderId(), OrderType.DINE_IN, OrderStatus.COMPLETED);
    }

    @Override
    public OrderDTO completeTakeAwayOrder(String username, String orderId) {
        log.info("completeTakeAwayOrder(username={}, orderId={})", username, orderId);
        OrderDTO orderDTO = orderReaderService.getOrder(orderId, OrderType.TAKE_AWAY);
        return orderUpdateService.updateOrderStatus(orderDTO.getOrderId(), OrderType.TAKE_AWAY, OrderStatus.COMPLETED);
    }
}
