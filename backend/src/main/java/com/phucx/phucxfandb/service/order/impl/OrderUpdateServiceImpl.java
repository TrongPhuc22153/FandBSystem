package com.phucx.phucxfandb.service.order.impl;

import com.phucx.phucxfandb.enums.OrderItemStatus;
import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.WaitListStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderDetailsMapper;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.service.address.ShippingAddressReaderService;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import com.phucx.phucxfandb.service.waitlist.WaitListReaderService;
import com.phucx.phucxfandb.utils.PriceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderUpdateServiceImpl implements OrderUpdateService {
    private final ShippingAddressReaderService shippingAddressReaderService;
    private final WaitListReaderService waitListReaderService;
    private final CustomerReaderService customerReaderService;
    private final EmployeeReaderService employeeReaderService;
    private final ProductReaderService productReaderService;
    private final ProductUpdateService productUpdateService;
    private final OrderRepository orderRepository;
    private final OrderDetailsMapper orderDetailsMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO updateOrderStatusByEmployee(String username, String orderID, OrderType type, OrderStatus status) {
        Order order = orderRepository.findByOrderIdAndEmployeeProfileUserUsername(orderID, username)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderID));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatusByCustomer(String username, String orderId, OrderType type, OrderStatus status) {
        Order order = orderRepository.findByOrderIdAndCustomerProfileUserUsername(orderId, username)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(String orderID, OrderType type, OrderStatus status) {
        Order order = orderRepository.findByOrderIdAndType(orderID, type)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderID));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(String username, String orderId, RequestOrderDTO requestOrderDTO) {
        Order order = orderRepository.findByOrderIdAndType(orderId, requestOrderDTO.getType())
                .orElseThrow(() -> new NotFoundException("Order", "id", orderId));

        validateOrderStatus(order);
        processOrderDetails(order, requestOrderDTO.getOrderDetails());
        updateOrderStatus(order);
        updateOrderFinancials(order);

        Order updated = orderRepository.save(order);
        return orderMapper.toOrderDTO(updated);
    }

    private void validateOrderStatus(Order order) {
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new IllegalStateException("Cannot modify order with status: " + order.getStatus());
        }
    }

    private void processOrderDetails(Order order, List<RequestOrderDetailsDTO> requestDetails) {
        for (RequestOrderDetailsDTO requestDetail : requestDetails) {
            validateRequestDetail(requestDetail);
            Optional<OrderDetail> existingDetail = findExistingOrderDetail(order, requestDetail);
            handleOrderDetail(order, requestDetail, existingDetail);
        }
    }

    private void validateRequestDetail(RequestOrderDetailsDTO requestDetail) {
        if (requestDetail.getProductId() == null || requestDetail.getQuantity() == null || requestDetail.getQuantity() < 0) {
            throw new IllegalArgumentException("productId and non-negative quantity are required");
        }
    }

    private Optional<OrderDetail> findExistingOrderDetail(Order order, RequestOrderDetailsDTO requestDetail) {
        if (requestDetail.getId() != null) {
            return order.getOrderDetails().stream()
                    .filter(d -> d.getId().equals(requestDetail.getId()))
                    .findFirst();
        }
        return order.getOrderDetails().stream()
                .filter(d -> d.getProduct().getProductId().equals(requestDetail.getProductId()))
                .findFirst();
    }

    private void handleOrderDetail(Order order, RequestOrderDetailsDTO requestDetail, Optional<OrderDetail> existingDetail) {
        if (existingDetail.isPresent()) {
            OrderDetail detail = existingDetail.get();
            if (isPreparedOrCompleted(detail)) {
                handlePreparedOrCompletedDetail(order, requestDetail, detail);
            } else {
                handlePendingOrPreparingDetail(order, requestDetail, detail);
            }
        } else if (requestDetail.getQuantity() > 0) {
            addNewOrderDetail(order, requestDetail);
        }
    }

    private boolean isPreparedOrCompleted(OrderDetail detail) {
        return OrderItemStatus.PREPARED.equals(detail.getStatus()) || OrderItemStatus.COMPLETED.equals(detail.getStatus());
    }

    private void handlePreparedOrCompletedDetail(Order order, RequestOrderDetailsDTO requestDetail, OrderDetail existingDetail) {
        if (requestDetail.getQuantity() > existingDetail.getQuantity()) {
            int additionalQuantity = requestDetail.getQuantity() - existingDetail.getQuantity();
            validateStock(requestDetail.getProductId(), additionalQuantity);
            addNewOrderDetail(order, requestDetail, additionalQuantity);
        }
    }

    private void handlePendingOrPreparingDetail(Order order, RequestOrderDetailsDTO requestDetail, OrderDetail detail) {
        if (requestDetail.getQuantity().equals(detail.getQuantity())) {
            detail.setSpecialInstruction(requestDetail.getSpecialInstruction());
        } else if (requestDetail.getQuantity() == 0) {
            order.getOrderDetails().remove(detail);
        } else {
            int quantityDelta = requestDetail.getQuantity() - detail.getQuantity();
            if (quantityDelta > 0) {
                validateStock(requestDetail.getProductId(), quantityDelta);
            }
            detail.setQuantity(requestDetail.getQuantity());
            detail.setSpecialInstruction(requestDetail.getSpecialInstruction());
        }
    }

    private void addNewOrderDetail(Order order, RequestOrderDetailsDTO requestDetail) {
        addNewOrderDetail(order, requestDetail, requestDetail.getQuantity());
    }

    private void addNewOrderDetail(Order order, RequestOrderDetailsDTO requestDetail, int quantity) {
        validateStock(requestDetail.getProductId(), quantity);
        Product product = productReaderService.getProductEntity(requestDetail.getProductId());
        OrderDetail newDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .unitPrice(product.getUnitPrice())
                .quantity(quantity)
                .status(OrderItemStatus.PENDING)
                .specialInstruction(requestDetail.getSpecialInstruction())
                .build();
        order.getOrderDetails().add(newDetail);
    }

    private void validateStock(long productId, int quantity) {
        Product product = productReaderService.getProductEntity(productId);
        if (product.getUnitsInStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
    }

    private void updateOrderStatus(Order order) {
        if (OrderStatus.PENDING.equals(order.getStatus()) ||
                OrderStatus.CONFIRMED.equals(order.getStatus()) ||
                OrderStatus.PREPARED.equals(order.getStatus()) ||
                OrderStatus.COMPLETED.equals(order.getStatus())) {
            order.setStatus(OrderStatus.PENDING);
        }
    }

    private void updateOrderFinancials(Order order) {
        order.setTotalPrice(PriceUtils.calculateOrderTotalPrice(order.getOrderDetails()));
        order.getPayment().setAmount(order.getTotalPrice());
    }

    @Override
    @Transactional
    public OrderDTO createOrderCustomer(String username, RequestOrderDTO requestOrderDTO) {
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);

        ShippingAddress shippingAddress = shippingAddressReaderService
                .getShippingAddressEntity(requestOrderDTO.getShippingAddressId());

        UserProfile userProfile = shippingAddress.getCustomer().getProfile();
        if (!(userProfile.getUser().getUsername().equals(username))) {
            throw new IllegalArgumentException("Username does not match the shipping address owner");
        }

        Order newOrder = orderMapper.toCustomerOrder(
                requestOrderDTO,
                customer,
                shippingAddress
        );

        List<OrderDetail> newOrderDetails = requestOrderDTO.getOrderDetails().stream()
                .map(item -> {
                    Product product = productReaderService.getProductEntity(item.getProductId());
                    if(product.getUnitsInStock()<item.getQuantity()){
                        throw new IllegalArgumentException("Insufficient stock for product ID: " + item.getProductId());
                    }
                    productUpdateService.updateProductInStock(item.getProductId(), product.getUnitsInStock() - item.getQuantity());
                    return orderDetailsMapper.toOrderDetail(item, product, newOrder);
                })
                .collect(Collectors.toList());

        BigDecimal totalPrice = PriceUtils.calculateOrderTotalPrice(newOrderDetails);

        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setOrderDetails(newOrderDetails);

        Payment payment = Payment.builder()
                .order(newOrder)
                .amount(totalPrice)
                .customer(customer)
                .build();

        newOrder.setPayment(payment);

        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO createOrderEmployee(String username, RequestOrderDTO requestOrderDTO) {
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);

        WaitList waitList = waitListReaderService.getWaitListEntity(requestOrderDTO.getWaitingListId());
        if(!waitList.getStatus().equals(WaitListStatus.SEATED)){
            throw new IllegalStateException("Cannot perform operation: WaitList with ID " + requestOrderDTO.getWaitingListId() + " must be SEATED. Current status: " + waitList.getStatus());
        }

        Order newOrder = orderMapper.toEmployeeOrder(
                requestOrderDTO,
                employee,
                waitList
        );
        newOrder.setStatus(OrderStatus.PENDING);

        List<OrderDetail> newOrderDetails = requestOrderDTO.getOrderDetails().stream()
                .map(item -> {
                    Product product = productReaderService.getProductEntity(item.getProductId());
                    if(product.getUnitsInStock()<item.getQuantity()){
                        throw new IllegalArgumentException("Insufficient stock for product ID: " + item.getProductId());
                    }
                    return orderDetailsMapper.toOrderDetail(item, product, newOrder);
                }).collect(Collectors.toList());

        BigDecimal totalPrice = PriceUtils.calculateOrderTotalPrice(newOrderDetails);
        newOrder.setOrderDetails(newOrderDetails);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);

        Payment payment = Payment.builder()
                .order(newOrder)
                .amount(totalPrice)
                .employee(employee)
                .build();

        newOrder.setPayment(payment);

        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }
}
