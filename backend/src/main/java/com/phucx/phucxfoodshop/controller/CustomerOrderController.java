package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.annotations.LoggerAspect;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.dto.InvoiceDetails;
import com.phucx.phucxfoodshop.model.dto.OrderDetails;
import com.phucx.phucxfoodshop.model.dto.OrderWithProducts;
import com.phucx.phucxfoodshop.model.dto.PaymentResponse;
import com.phucx.phucxfoodshop.service.order.CustomerOrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/order/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    // ENDPOINT TO PLACE AN ORDER
    @LoggerAspect
    @Operation(summary = "Place an order", tags = {"order", "post", "customer"})
    @PostMapping("/order/place")
    public ResponseEntity<PaymentResponse> placeOrder(
        @RequestBody OrderWithProducts order, 
        Authentication authentication
    ) throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException, CustomerNotFoundException, PayPalRESTException{

        PaymentResponse paymentResponse = customerOrderService
            .placeOrderByUsername(order, authentication.getName());
        return ResponseEntity.ok().body(paymentResponse);
    }

    @LoggerAspect
    @Operation(summary = "Receive customer order", tags = {"order", "post", "customer"})
    @PostMapping("/order/receive")
    public ResponseEntity<Void> receiveOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
        throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, CustomerNotFoundException, PaymentNotFoundException{
        customerOrderService.receiveOrder(order);
        return ResponseEntity.ok().build();
    }
    
    // get INVOICE of customer
    @Operation(summary = "Get order by id", tags = {"order", "get", "customer"})
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<InvoiceDetails> getOrder(@PathVariable String orderID, Authentication authentication) 
    throws JsonProcessingException, ShipperNotFoundException, EmployeeNotFoundException, NotFoundException{    
        InvoiceDetails order = customerOrderService.getInvoiceByUsername(orderID, authentication.getName());
        return ResponseEntity.ok().body(order);
    }
    // GET ALL ORDERS OF CUSTOMER
    @Operation(summary = "Get orders", tags = {"order", "get", "customer"})
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetails>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, CustomerNotFoundException{    
        pageNumber = pageNumber!=null?pageNumber:0;
        OrderStatus status = null;
        if(orderStatus!=null){
            status = OrderStatus.fromString(orderStatus.toUpperCase());
        }else {
            status=OrderStatus.All;
        }
        Page<OrderDetails> orders = customerOrderService.getOrdersByUsername(
            pageNumber, WebConstant.PAGE_SIZE, authentication.getName(), status);
        return ResponseEntity.ok().body(orders);
    }
}
