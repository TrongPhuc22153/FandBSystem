package com.phucx.phucxfandb.service.order.impl;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.*;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.enums.*;
import com.phucx.phucxfandb.service.cart.CartUpdateService;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.order.OrderDetailService;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {
    private final CartUpdateService cartUpdateService;
    private final OrderUpdateService orderUpdateService;
    private final OrderReaderService orderReaderService;
    private final OrderDetailService orderDetailService;
    private final TableOccupancyUpdateService tableOccupancyUpdateService;
    private final SendOrderNotificationService sendOrderNotificationService;

    @Override
    @Transactional
    public OrderDTO cancelOrderByEmployee(String username, String orderId, OrderType type) {
        validateOrder(orderId, type);

        OrderDTO orderDTO = orderUpdateService.updateOrderStatusByEmployee(username, orderId, type, OrderStatus.CANCELLED);
        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.CANCELED);

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
    @Transactional
    public OrderDTO cancelOrderByCustomer(String username, String orderId, OrderType type) {
        validateOrder(orderId, type);

        OrderDTO orderDTO = orderUpdateService.updateOrderStatusByCustomer(username, orderId, type, OrderStatus.CANCELLED);
        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.CANCELED);

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

    private void validateOrder(String orderId, OrderType type){
        Order order = orderReaderService.getOrderEntity(orderId, type);
        if(!OrderStatus.PENDING.equals(order.getStatus())){
            throw new IllegalStateException("Cannot cancel in process order");
        }

        for (OrderDetail itemToCancel: order.getOrderDetails()){
            if (!EnumSet.of(OrderItemStatus.PENDING, OrderItemStatus.PREPARING).contains(itemToCancel.getStatus())) {
                throw new IllegalStateException("Only items in PENDING or PREPARING status can be canceled.");
            }
        }
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
    @Transactional
    public OrderDTO markOrderAsPrepared(String username, String orderId, OrderType type) {
        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.PREPARING, OrderItemStatus.PREPARED);
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.PREPARED);
    }

    @Override
    @Transactional
    public OrderDTO markOrderAsServed(Authentication authentication, String orderId, OrderType type) {
        Order order = orderReaderService.getOrderEntity(orderId, type);
        var orderItemStatuses = EnumSet.of(OrderItemStatus.SERVED, OrderItemStatus.PREPARED);
        for (OrderDetail detail : order.getOrderDetails()) {
            if (!orderItemStatuses.contains(detail.getStatus())) {
                throw new IllegalStateException("There are still in process food");
            }
        }
        if (!(OrderStatus.READY_TO_SERVE.equals(order.getStatus()) ||
                OrderStatus.PARTIALLY_SERVED.equals(order.getStatus()))) {
            throw new IllegalStateException("Order is not in a state that can be marked as served.");
        }

        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.SERVED);
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.SERVED);
    }

    @Override
    @Transactional
    public OrderDTO preparingOrder(String username, String orderId, OrderType type) {
        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.PENDING, OrderItemStatus.PREPARING);
        return orderUpdateService.updateOrderStatus(orderId, type, OrderStatus.PREPARING);
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(RequestOrderDTO requestOrderDTO, Authentication authentication) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        OrderDTO newOrder;

        if(roleNames.contains(RoleName.CUSTOMER) && requestOrderDTO.getType().equals(OrderType.TAKE_AWAY)){
            newOrder = this.placeOrderByCustomer(authentication.getName(), requestOrderDTO);
        }else if(roleNames.contains(RoleName.EMPLOYEE) && requestOrderDTO.getType().equals(OrderType.DINE_IN)){
            newOrder = this.placeOrderByEmployee(authentication.getName(), requestOrderDTO);
        }else{
            throw new IllegalArgumentException("Invalid order type");
        }

        return newOrder;
    }

    @Override
    public OrderDTO placeOrderByCustomer(String username, RequestOrderDTO requestOrderDTO) {
        List<Long> productIds = requestOrderDTO.getOrderDetails().stream()
                .map(RequestOrderDetailsDTO::getProductId)
                .toList();
        OrderDTO newOrder = orderUpdateService.createOrderCustomer(username, requestOrderDTO);

        if(newOrder.getPayment().getStatus().equals(PaymentStatus.SUCCESSFUL)){
            cartUpdateService.removeCartItems(username, productIds);
        }
        return newOrder;
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
    @Transactional
    public OrderDTO completeDineInOrder(String username, String orderId){
        Order order = orderReaderService.getOrderEntity(orderId, OrderType.DINE_IN);
        if(!OrderStatus.SERVED.equals(order.getStatus())){
            throw new IllegalStateException("Order is not served");
        }
        order.getOrderDetails().forEach(item -> {
            if(!EnumSet.of(OrderItemStatus.SERVED, OrderItemStatus.CANCELED).contains(item.getStatus())){
                throw new IllegalStateException("There are still ongoing foods");
            }
        });

        tableOccupancyUpdateService.updateTableOccupancyStatus(
                order.getTableOccupancy().getId(),
                order.getTableOccupancy().getTable().getTableId(),
                TableOccupancyStatus.COMPLETED);

        return orderUpdateService.updateOrderStatus(orderId, OrderType.DINE_IN, OrderStatus.COMPLETED);
    }

    @Override
    @Transactional
    public OrderDTO completeTakeAwayOrder(String username, String orderId) {
        OrderDTO order = orderReaderService.getOrder(orderId, OrderType.TAKE_AWAY);
        if(!OrderStatus.READY_TO_PICKUP.equals(order.getStatus())){
            throw new IllegalStateException("Order is not ready to pickup");
        }
        order.getOrderDetails().forEach(item -> {
            if(!EnumSet.of(OrderItemStatus.PREPARED, OrderItemStatus.CANCELED).contains(item.getStatus())){
                throw new IllegalStateException("There are still ongoing foods");
            }
        });
        orderDetailService.updateOrderItemStatus(orderId, OrderItemStatus.PREPARED, OrderItemStatus.SERVED);
        return orderUpdateService.updateOrderStatus(orderId, OrderType.TAKE_AWAY, OrderStatus.COMPLETED);
    }

    @Override
    @Transactional
    public OrderDTO markDineInOrderAsReadyToServe(String username, String orderId) {
        return orderUpdateService.updateOrderStatus(orderId, OrderType.DINE_IN, OrderStatus.READY_TO_SERVE);
    }

    @Override
    @Transactional
    public OrderDTO markTakeAwayOrderAsReadyToPick(String username, String orderId) {
        return orderUpdateService.updateOrderStatus(orderId, OrderType.DINE_IN, OrderStatus.READY_TO_PICKUP);
    }

    @Override
    public OrderDTO markOrderAsReady(Authentication authentication, String orderId, OrderType type) {
        return switch (type){
            case DINE_IN -> this.markDineInOrderAsReadyToServe(authentication.getName(), orderId);
            case TAKE_AWAY -> this.markTakeAwayOrderAsReadyToPick(authentication.getName(), orderId);
        };
    }

    @Override
    public OrderDTO processOrder(Authentication authentication, String orderId, OrderAction action, OrderType type) {
        OrderDTO result = switch (action){
            case PREPARING -> this.preparingOrder(authentication.getName(), orderId, type);
            case PREPARED -> this.markOrderAsPrepared(authentication.getName(), orderId, type);
            case READY -> this.markOrderAsReady(authentication, orderId, type);
            case SERVED -> this.markOrderAsServed(authentication, orderId, type);
            case COMPLETE -> this.completeOrder(authentication.getName(), orderId, type);
            case CANCEL -> this.cancelOrder(orderId, type, authentication);
        };

        sendOrderNotificationService.sendNotificationForOrderAction(authentication, orderId, action, type, result);

        return result;
    }
}
