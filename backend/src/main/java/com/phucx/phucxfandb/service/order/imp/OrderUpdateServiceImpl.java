package com.phucx.phucxfandb.service.order.imp;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderDetailsMapper;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderUpdateServiceImpl implements OrderUpdateService {
    private final ReservationTableReaderService reservationTableReaderService;
    private final ReservationTableUpdateService reservationTableUpdateService;
    private final CustomerReaderService customerReaderService;
    private final EmployeeReaderService employeeReaderService;
    private final ProductReaderService productReaderService;
    private final OrderRepository orderRepository;
    private final OrderDetailsMapper orderDetailsMapper;
    private final OrderMapper orderMapper;

    @Override
    @Modifying
    @Transactional
    public OrderDTO updateOrderStatusAndEmployee(String username, String orderID, OrderType type, OrderStatus status) {
        log.info("updateOrderStatusAndEmployee(username={}, orderID={}, type={} status={})",
                username, orderID, type, status);
        Order order = orderRepository.findByOrderIdAndType(orderID, type)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderID));
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);
        order.setEmployee(employee);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO updateOrderStatusByEmployee(String username, String orderID, OrderType type, OrderStatus status) {
        log.info("updateOrderStatusByEmployee(username={}, orderID={}, type={}, status={})",
                username, orderID, type, status);
        Order order = orderRepository.findByOrderIdAndEmployeeProfileUserUsername(orderID, username)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderID));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO updateOrderStatusByCustomer(String username, String orderId, OrderType type, OrderStatus status) {
        log.info("updateOrderStatusByCustomer(username={}, orderId={}, type={} status={})",
                username, orderId, type, status);
        Order order = orderRepository.findByOrderIdAndCustomerProfileUserUsername(orderId, username)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO updateOrderStatus(String orderID, OrderType type, OrderStatus status) {
        log.info("updateOrderStatus(orderID={}, type={}, status={})", orderID, type, status);
        Order order = orderRepository.findByOrderIdAndType(orderID, type)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderID));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO updateOrderStatus(String orderID, OrderStatus status) {
        log.info("updateOrderStatus(orderID={}, status={})", orderID, status);
        Order order = orderRepository.findById(orderID)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderID));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO createOrderCustomer(String username, RequestOrderDTO requestOrderDTO) {
        log.info("createOrderCustomer(username={}, requestOrderDTO={})", username, requestOrderDTO);
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);
        Order newOrder = orderMapper.toCustomerOrder(
                requestOrderDTO,
                customer);
        newOrder.setStatus(OrderStatus.PENDING);

        List<OrderDetail> newOrderDetails = requestOrderDTO.getOrderDetails().stream()
                .map(requestOrderDetail -> {
                    Product product = productReaderService.getProductEntity(requestOrderDetail.getProductId());
                    return orderDetailsMapper.toOrderDetail(requestOrderDetail, product, newOrder);
                })
                .collect(Collectors.toList());

        newOrder.setOrderDetails(newOrderDetails);
        newOrder.setTotalPrice(calculateTotalPrice(newOrderDetails));

        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public OrderDTO createOrderEmployee(String username, RequestOrderDTO requestOrderDTO) {
        log.info("createOrderEmployee(username={}, requestOrderDTO={})", username, requestOrderDTO);
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);
        // Get table
        ReservationTable table = reservationTableReaderService
                .getReservationTableEntity(requestOrderDTO.getTableId());

        if(table.getStatus().equals(TableStatus.UNOCCUPIED)){
            reservationTableUpdateService.updateTableStatus(table.getTableId(), TableStatus.OCCUPIED);
        }
        // Create new order
        Order newOrder = orderMapper.toEmployeeOrder(
                requestOrderDTO,
                employee, table
        );
        newOrder.setStatus(OrderStatus.PENDING);

        List<OrderDetail> newOrderDetails = requestOrderDTO.getOrderDetails().stream()
                .map(requestOrderDetail -> {
                    Product product = productReaderService.getProductEntity(requestOrderDetail.getProductId());
                    return orderDetailsMapper.toOrderDetail(requestOrderDetail, product, newOrder);
                }).collect(Collectors.toList());

        newOrder.setOrderDetails(newOrderDetails);
        newOrder.setTotalPrice(calculateTotalPrice(newOrderDetails));

        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }

    private BigDecimal calculateTotalPrice(List<OrderDetail> orderDetails) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails) {
            totalPrice = totalPrice.add(orderDetail.getUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
        }
        return totalPrice;
    }
}
