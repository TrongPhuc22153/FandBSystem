package com.phucx.phucxfoodshop.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.PaymentResponse;

public interface CustomerOrderService {
    // place an order by customer
    public PaymentResponse placeOrder(OrderWithProducts order, String userID) throws JsonProcessingException,CustomerNotFoundException, InvalidDiscountException, PayPalRESTException, InvalidOrderException, NotFoundException;
    public PaymentResponse placeOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException,CustomerNotFoundException, InvalidDiscountException, PayPalRESTException, InvalidOrderException, NotFoundException;
    // receive order
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException, PaymentNotFoundException, NotFoundException, EmployeeNotFoundException, CustomerNotFoundException;
    // get customer's orders
    public Page<OrderDetails> getOrders(int pageNumber, int pageSize, String userID, OrderStatus orderStatus) throws JsonProcessingException, NotFoundException,ShipperNotFoundException, CustomerNotFoundException;
    public Page<OrderDetails> getOrdersByUsername(int pageNumber, int pageSize, String username, OrderStatus orderStatus) throws JsonProcessingException, NotFoundException,ShipperNotFoundException, CustomerNotFoundException;
    // get customer 's invoice
    public InvoiceDetails getInvoice(String orderID, String userID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, ShipperNotFoundException, EmployeeNotFoundException;
    public InvoiceDetails getInvoiceByUsername(String orderID, String username) throws JsonProcessingException, ShipperNotFoundException, EmployeeNotFoundException, NotFoundException;
}