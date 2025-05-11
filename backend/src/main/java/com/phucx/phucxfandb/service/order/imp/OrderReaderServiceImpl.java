package com.phucx.phucxfandb.service.order.imp;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.request.OrderRequestParamDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.OrderMapper;
import com.phucx.phucxfandb.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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
    public Order getOrderEntity(String orderId, OrderType type) {
        log.info("getOrderEntity(orderId={}, type={})", orderId, type);
        return orderRepository.findByOrderIdAndType(orderId, type)
                .orElseThrow(()-> new NotFoundException("Order", "id", orderId));
    }

    @Override
    public Page<OrderDTO> getOrders(OrderRequestParamDTO params, Authentication authentication) {
        List<RoleName> roles = RoleUtils.getRoles(authentication.getAuthorities());
        if (roles.contains(RoleName.ADMIN)) {
            return getAdminOrders(params);
        } else {
            return getCustomerOrders(authentication.getName(), params);
        }
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getCustomerOrders(String username, OrderRequestParamDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasCustomerUsername(username))
                .and(OrderSpecification.hasType(params.getType()))
                .and(OrderSpecification.hasStatus(params.getStatus()));
        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderListEntryDTO);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getAdminOrders(OrderRequestParamDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasStatus(params.getStatus()))
                .and(OrderSpecification.hasType(params.getType()));
        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderListEntryDTO);
    }
}
