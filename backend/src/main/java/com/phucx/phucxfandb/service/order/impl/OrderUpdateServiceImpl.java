package com.phucx.phucxfandb.service.order.impl;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.constant.WaitListStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
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
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderUpdateServiceImpl implements OrderUpdateService {
    private final ShippingAddressReaderService shippingAddressReaderService;
    private final WaitListReaderService waitListReaderService;
    private final PaymentUpdateService paymentUpdateService;
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

        RequestPaymentDTO requestPaymentDTO = requestOrderDTO.getPayment();
        Payment payment = paymentUpdateService.createCustomerPayment(
                requestPaymentDTO.getPaymentMethod(),
                totalPrice,
                customer.getCustomerId()
        );

        newOrder.setPayment(payment);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setOrderDetails(newOrderDetails);

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
                .map(requestOrderDetail -> {
                    Product product = productReaderService.getProductEntity(requestOrderDetail.getProductId());
                    return orderDetailsMapper.toOrderDetail(requestOrderDetail, product, newOrder);
                }).collect(Collectors.toList());

        BigDecimal totalPrice = PriceUtils.calculateOrderTotalPrice(newOrderDetails);

        RequestPaymentDTO requestPaymentDTO = requestOrderDTO.getPayment();
        Payment payment = paymentUpdateService.createEmployeePayment(
                requestPaymentDTO.getPaymentMethod(),
                totalPrice,
                employee.getEmployeeId()
        );

        newOrder.setPayment(payment);
        newOrder.setOrderDetails(newOrderDetails);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }
}
