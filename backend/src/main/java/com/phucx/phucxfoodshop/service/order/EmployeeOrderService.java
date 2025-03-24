package com.phucx.phucxfoodshop.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderWithProducts;

public interface EmployeeOrderService {
    // processing order of customer
    public void confirmOrder(String orderID, String userID) throws InvalidOrderException, EmployeeNotFoundException, NotFoundException, JsonProcessingException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
    public void confirmOrderByUsername(String orderID, String username) throws InvalidOrderException, EmployeeNotFoundException, NotFoundException, JsonProcessingException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
    public void cancelPendingOrder(OrderWithProducts order, String userID) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException;
    public void cancelPendingOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException;
    public void cancelConfirmedOrder(OrderWithProducts order, String userID) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException;
    public void cancelConfirmedOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException;
    public void fullfillOrder(OrderWithProducts order, String userID) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException;
    public void fullfillOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException;
    // get orders
    public Page<OrderDetails> getOrders(String userID, OrderStatus status, int pageNumber, int pageSize) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException;
    public Page<OrderDetails> getOrdersByUsername(String username, OrderStatus status, int pageNumber, int pageSize) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException;
    // get order
    public OrderWithProducts getOrder(String orderID, String userID, OrderStatus status) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
    public OrderWithProducts getOrderByUsername(String orderID, String username, OrderStatus status) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
}
