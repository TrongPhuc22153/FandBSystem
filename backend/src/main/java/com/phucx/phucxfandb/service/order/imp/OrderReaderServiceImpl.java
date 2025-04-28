package com.phucx.phucxfandb.service.order.imp;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderReaderServiceImpl implements OrderReaderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(LocalDate date, int pageNumber, int pageSize) {
        log.info("getOrders(date={}, pageNumber={}, pageSize={})", date, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return orderRepository.findByOrderDateBetween(startOfDay, endOfDay, pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId) {
        log.info("getOrder(orderId={})", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId, OrderType type) {
        log.info("getOrder(orderId={}, type={})", orderId, type);
        Order order = orderRepository.findByOrderIdAndType(orderId, type)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntity(String orderId) {
        log.info("getOrderEntity(orderId={})", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId, OrderStatus status) {
        log.info("getOrder(orderId={}, status={})", orderId, status);
        Order order = orderRepository.findByStatusAndOrderId(status, orderId)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(OrderStatus status, int pageNumber, int pageSize) {
        log.info("getOrders(status={}, pageNumber={}, pageSize={})", status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findByStatus(status, pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(int pageNumber, int pageSize) {
        log.info("getOrders(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByTableId(String tableId) {
        log.info("getOrderByTableId(tableId={})", tableId);
        Order order = orderRepository.findByTableTableId(tableId)
                .orElseThrow(()-> new NotFoundException("Order", "table id", tableId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderIdAndCustomerUsername(String orderId, String username) {
        log.info("getOrderByOrderIdAndCustomerUsername(orderId={}, username={})", orderId, username);
        Order order = orderRepository.findByOrderIdAndCustomerProfileUserUsername(orderId, username)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByCustomerUsername(String username, int pageNumber, int pageSize) {
        log.info("getOrdersByCustomerUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findByCustomerProfileUserUsername(username, pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByCustomerUsername(String username, OrderStatus status, int pageNumber, int pageSize) {
        log.info("getOrdersByCustomerUsername(username={}, status={}, pageNumber={}, pageSize={})", username, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findByCustomerProfileUserUsernameAndStatus(username, status, pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderIdAndEmployeeUsername(String orderId, String username) {
        log.info("getOrderByOrderIdAndEmployeeUsername(orderId={}, username={})", orderId, username);
        Order order = orderRepository.findByOrderIdAndEmployeeProfileUserUsername(orderId, username)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByEmployeeUsername(String username, int pageNumber, int pageSize) {
        log.info("getOrdersByEmployeeUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findByEmployeeProfileUserUsername(username, pageable)
                .map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByEmployeeUsername(String username, OrderStatus status, int pageNumber, int pageSize) {
        log.info("getOrdersByEmployeeUsername(username={}, status={}, pageNumber={}, pageSize={})", username, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findByEmployeeEmployeeIdAndStatus(username, status, pageable)
                .map(orderMapper::toOrderDTO);
    }
}
