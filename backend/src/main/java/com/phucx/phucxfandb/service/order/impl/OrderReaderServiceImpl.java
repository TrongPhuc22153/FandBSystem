package com.phucx.phucxfandb.service.order.impl;

import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.request.OrderRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.specifications.OrderSpecification;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderReaderServiceImpl implements OrderReaderService {
    private final ImageReaderService imageReaderService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
        order.getOrderDetails().forEach(this::setImageUrl);
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId, OrderType type) {
        Order order = orderRepository.findByOrderIdAndType(orderId, type)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
        order.getOrderDetails().forEach(this::setImageUrl);
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntity(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntity(String orderId, OrderType type) {
        return orderRepository.findByOrderIdAndType(orderId, type)
                .orElseThrow(()-> new NotFoundException(Order.class.getSimpleName(), "id", orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(OrderRequestParamsDTO params, Authentication authentication) {
        List<RoleName> roles = RoleUtils.getRoles(authentication.getAuthorities());
        if (roles.contains(RoleName.CUSTOMER)) {
            return getCustomerOrders(authentication.getName(), params);
        } else {
            return getUserOrder(params);
        }
    }

    private Page<OrderDTO> getCustomerOrders(String username, OrderRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasCustomerUsername(username))
                .and(OrderSpecification.hasType(params.getType()))
                .and(OrderSpecification.hasStatuses(params.getStatus()))
                .and(OrderSpecification.searchOrders(params.getSearch()))
                .and(OrderSpecification.hasOrderDateBetween(params.getStartDate(), params.getEndDate()));

        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderListEntryDTO);
    }

    private Page<OrderDTO> getUserOrder(OrderRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasStatuses(params.getStatus()))
                .and(OrderSpecification.hasType(params.getType()))
                .and(OrderSpecification.searchOrders(params.getSearch()))
                .and(OrderSpecification.hasOrderDateBetween(params.getStartDate(), params.getEndDate()));

        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderListEntryDTO);
    }

    private void setImageUrl(OrderDetail item){
        if(!(item.getProduct().getPicture()==null || item.getProduct().getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(item.getProduct().getPicture());
            item.getProduct().setPicture(imageUrl);
        }
    }
}
