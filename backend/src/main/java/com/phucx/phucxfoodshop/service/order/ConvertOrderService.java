package com.phucx.phucxfoodshop.service.order;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.Invoice;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.Order;
import com.phucx.phucxfoodshop.model.OrderDetailExtended;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderWithProducts;

public interface ConvertOrderService {
    public InvoiceDetails convertInvoiceDetails(List<Invoice> invoices) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, EmployeeNotFoundException;
    public List<OrderDetails> convertOrders(List<OrderDetailExtended> orders) throws JsonProcessingException, NotFoundException;
    public OrderDetails convertOrderDetail(List<OrderDetailExtended> orderDetailExtendeds) throws JsonProcessingException, NotFoundException, CustomerNotFoundException;
    public OrderWithProducts convertOrderWithProducts(Order order) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, ShipperNotFoundException;
}
