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
import com.phucx.phucxfandb.utils.NotificationUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {
    private final OrderUpdateService orderUpdateService;
    private final OrderReaderService orderReaderService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final ReservationTableUpdateService reservationTableUpdateService;

    @Override
    public OrderDTO cancelOrderByEmployee(String username, String orderId, OrderType type) {
        OrderDTO orderDTO = orderUpdateService.updateOrderStatusByEmployee(username, orderId, type, OrderStatus.CANCELLED);

        RequestNotificationDTO requestNotificationDTO = NotificationUtils.createRequestNotificationDTO(
                username,
                orderDTO.getEmployee().getProfile().getUser().getUsername(),
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_CANCELLED,
                NotificationMessage.ORDER_CANCELLED_MESSAGE
        );

        sendOrderNotificationService.sendNotificationToUser(
                orderDTO.getOrderId(),
                requestNotificationDTO
        );

        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrderByCustomer(String username, String orderId, OrderType type) {
        OrderDTO orderDTO = orderUpdateService.updateOrderStatusByCustomer(username, orderId, type, OrderStatus.CANCELLED);

        RequestNotificationDTO requestNotificationDTO = NotificationUtils.createRequestNotificationDTO(
                username,
                orderDTO.getCustomer().getProfile().getUser().getUsername(),
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_CANCELLED,
                NotificationMessage.ORDER_CANCELLED_MESSAGE
        );

        sendOrderNotificationService.sendNotificationToUser(
                orderDTO.getOrderId(),
                requestNotificationDTO
        );

        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrder(String orderId, OrderType type, Authentication authentication) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        if(roleNames.contains(RoleName.CUSTOMER) && type.equals(OrderType.TAKE_AWAY)){
            return this.cancelOrderByCustomer(authentication.getName(), orderId, type);
        }else if(roleNames.contains(RoleName.EMPLOYEE) && type.equals(OrderType.DINE_IN)){
            return this.cancelOrderByEmployee(authentication.getName(), orderId, type);
        }else{
            throw new IllegalArgumentException("Invalid order type and role");
        }
    }

    @Override
    public OrderDTO markOrderAsPrepared(String username, String orderId, OrderType type) {
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.PREPARED);
    }

    @Override
    public OrderDTO preparingOrder(String username, String orderId, OrderType type) {
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.PREPARING);
    }

    @Override
    public OrderDTO placeOrder(RequestOrderDTO requestOrderDTO, Authentication authentication) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        OrderDTO result;

        if(roleNames.contains(RoleName.CUSTOMER) && requestOrderDTO.getType().equals(OrderType.TAKE_AWAY)){
            result = this.placeOrderByCustomer(authentication.getName(), requestOrderDTO);
        }else if(roleNames.contains(RoleName.EMPLOYEE) && requestOrderDTO.getType().equals(OrderType.DINE_IN)){
            result = this.placeOrderByEmployee(authentication.getName(), requestOrderDTO);
        }else{
            throw new IllegalArgumentException("Invalid order type");
        }

        sendOrderNotificationService.sendPlaceOrderNotification(
                authentication,
                result.getOrderId(),
                result.getType(),
                result
        );

        return result;
    }

    @Override
    public OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO) {
        return orderUpdateService.createOrderCustomer(username, requestOrderDTO);
    }

    @Override
    public OrderDTO placeOrderByEmployee(String username, RequestOrderDTO requestOrderDTO) {
        return orderUpdateService.createOrderEmployee(username, requestOrderDTO);
    }

    @Override
    public OrderDTO completeOrder(String username, String orderId, OrderType type) {
        return switch (type) {
            case DINE_IN -> this.completeDineInOrder(username, orderId);
            case TAKE_AWAY -> this.completeTakeAwayOrder(username, orderId);
        };
    }

    @Override
    public OrderDTO completeDineInOrder(String username, String orderId){
        Order order = orderReaderService.getOrderEntity(orderId, OrderType.DINE_IN);
        if(!order.getOrderId().equals(orderId)){
            throw new NotFoundException(String.format("Table with id %s and order with id %s not found", order.getTable().getTableId(), orderId));
        }
        reservationTableUpdateService.updateTableStatus(order.getTable().getTableId(), TableStatus.UNOCCUPIED);
        return orderUpdateService.updateOrderStatus(order.getOrderId(), OrderType.DINE_IN, OrderStatus.COMPLETED);
    }

    @Override
    public OrderDTO completeTakeAwayOrder(String username, String orderId) {
        OrderDTO orderDTO = orderReaderService.getOrder(orderId, OrderType.TAKE_AWAY);
        return orderUpdateService.updateOrderStatus(orderDTO.getOrderId(), OrderType.TAKE_AWAY, OrderStatus.COMPLETED);
    }

    @Override
    public OrderDTO processOrder(Authentication authentication, String orderId, OrderAction action, OrderType type) {
        OrderDTO result = switch (action){
            case PREPARING -> this.preparingOrder(authentication.getName(), orderId, type);
            case READY -> this.markOrderAsPrepared(authentication.getName(), orderId, type);
            case COMPLETE -> this.completeOrder(authentication.getName(), orderId, type);
            case CANCEL -> this.cancelOrder(orderId, type, authentication);
        };

        sendOrderNotificationService.sendNotificationForOrderAction(authentication, orderId, action, type, result);

        return result;
    }
}
