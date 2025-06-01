package com.phucx.phucxfandb.service.order.impl;

import com.phucx.phucxfandb.enums.OrderItemStatus;
import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.service.order.OrderDetailService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.utils.PriceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final ProductReaderService productReaderService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO updateOrderItemQuantity(String orderId, String orderItemId, RequestOrderDetailsDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class.getSimpleName(), "id", orderId));

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify order with status: " + order.getStatus());
        }

        OrderDetail existingItem = order.getOrderDetails().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(OrderDetail.class.getSimpleName(), "id", orderItemId));

        if (!existingItem.getProduct().getProductId().equals(request.getProductId())) {
            throw new IllegalArgumentException("Provided productId does not match the existing OrderDetail");
        }

        if (request.getQuantity() == 0) {
            order.getOrderDetails().remove(existingItem);
        } else {
            int quantityDelta = request.getQuantity() - existingItem.getQuantity();
            if (quantityDelta > 0) {
                Product product = productReaderService.getProductEntity(request.getProductId());
                if (product.getUnitsInStock() < quantityDelta) {
                    throw new IllegalArgumentException("Insufficient stock for product ID: " + request.getProductId());
                }
            }

            existingItem.setQuantity(request.getQuantity());
            existingItem.setSpecialInstruction(request.getSpecialInstruction());
        }

        return saveAndMapOrder(order);
    }

    @Override
    @Transactional
    public OrderDTO addOrderItem(String orderId, RequestOrderDetailsDTO request) {
        if (request.getQuantity() == 0) {
            throw new IllegalArgumentException("quantity must be positive for new OrderDetail");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class.getSimpleName(), "id", orderId));

        Product product = productReaderService.getProductEntity(request.getProductId());
        if (product.getUnitsInStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + request.getProductId());
        }

        OrderDetail newItem = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(product.getUnitPrice())
                .specialInstruction(request.getSpecialInstruction())
                .build();

        order.getOrderDetails().add(newItem);

        return saveAndMapOrder(order);
    }

    private OrderDTO saveAndMapOrder(Order order) {
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.PREPARING);
        }

        order.setTotalPrice(PriceUtils.calculateOrderTotalPrice(order.getOrderDetails()));
        order.getPayment().setAmount(order.getTotalPrice());

        Order updated = orderRepository.save(order);
        return orderMapper.toOrderDTO(updated);
    }

    @Override
    @Transactional
    public void updateOrderItemStatus(String orderId, OrderItemStatus originalStatus, OrderItemStatus statusToUpdate) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
        order.getOrderDetails().forEach(item -> {
            if(item.getStatus().equals(originalStatus)){
                item.setStatus(statusToUpdate);
            }
        });
        orderRepository.save(order);
    }
}
