package com.phucx.phucxfoodshop.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderSummary;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.ResponseFormat;

public interface OrderService {
    // check pending order
    public Boolean isPendingOrder(String orderID) throws NotFoundException;
    // update order status
    public Boolean updateOrderStatus(String orderID, OrderStatus status) throws NotFoundException;
    // save order of customer
    public String saveFullOrder(OrderWithProducts order) throws CustomerNotFoundException, NotFoundException;
    // validate customer's order 
    // validate order' products, discounts
    public Boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException;
    // validate order's products with product's in stocks and update it by message queue
    public ResponseFormat validateAndProcessOrder(OrderWithProducts order) throws JsonProcessingException;
    // update employee id to order
    public Boolean updateOrderEmployee(String orderID, String employeeID);
    // get order
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException, CustomerNotFoundException;
    public OrderDetails getOrder(String orderID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException;
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, CustomerNotFoundException, EmployeeNotFoundException;
    // get customer's order
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException, ShipperNotFoundException;
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, EmployeeNotFoundException;
    // get employee's order
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException;
    // count order by status
    public OrderSummary getOrderSummary();
} 
