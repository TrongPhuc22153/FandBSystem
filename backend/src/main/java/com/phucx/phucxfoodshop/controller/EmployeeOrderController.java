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
import com.phucx.phucxfoodshop.annotations.LoggerAspect;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.dto.OrderDetails;
import com.phucx.phucxfoodshop.model.dto.OrderSummary;
import com.phucx.phucxfoodshop.model.dto.OrderWithProducts;
import com.phucx.phucxfoodshop.service.order.EmployeeOrderService;
import com.phucx.phucxfoodshop.service.order.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/order/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeOrderController {
    private final EmployeeOrderService employeeOrderService;
    private final OrderService orderService;
    // CONFIRM AN ORDER
    @LoggerAspect
    @Operation(summary = "Confirm a pending order", tags = {"order", "post", "employee"})
    @PostMapping("/order/confirm")
    public ResponseEntity<Void> confirmOrder(
        @RequestBody OrderWithProducts order, 
        Authentication authentication
    ) throws InvalidOrderException, JsonProcessingException, NotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException{
        // // validate order
        employeeOrderService.confirmOrderByUsername(order.getOrderID(), authentication.getName());
        return ResponseEntity.ok().build();
    }
    // CANCEL AN ORDER
    @LoggerAspect
    @Operation(summary = "Cancel an order", tags = {"order", "post", "employee"})
    @PostMapping("/order/cancel")
    public ResponseEntity<Void> cancelOrder(
        @RequestBody OrderWithProducts order, 
        @RequestParam(name = "type") String ordertype,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, PaymentNotFoundException{
        // accept pending and confirmed for order type
        if(OrderStatus.Pending.name().equalsIgnoreCase(ordertype)){
            // cancel order
            employeeOrderService.cancelPendingOrderByUsername(order, authentication.getName());
        }else if(OrderStatus.Confirmed.name().equalsIgnoreCase(ordertype)){
            // cancel order
            employeeOrderService.cancelConfirmedOrderByUsername(order, authentication.getName());
        }
        return ResponseEntity.ok().build();
    }
    // FULFILL ORDER
    @LoggerAspect
    @Operation(summary = "Fulfill an order", tags = {"order", "post", "employee"})
    @PostMapping("/order/fulfill")
    public ResponseEntity<Void> fulfillOrder(
        @RequestBody OrderWithProducts order, Authentication authentication
    ) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, PaymentNotFoundException{
        // update order status
        employeeOrderService.fullfillOrderByUsername(order, authentication.getName());
        return ResponseEntity.ok().build();
    }


    // get order of emloyee
    @Operation(summary = "Get an order by id", tags = {"order", "get", "employee"})
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<OrderWithProducts> getOrder(
        @PathVariable(name = "orderID") String orderID, 
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException{    
        // get order's status
        OrderStatus status = orderStatus!=null?
            OrderStatus.fromString(orderStatus.toUpperCase()):
            OrderStatus.All;
        OrderWithProducts order = employeeOrderService.getOrderByUsername(
            orderID, authentication.getName(), status);
        return ResponseEntity.ok().body(order);
    }

    // GET ALL ORDERS WHICH EMPLOYEE HAS APPROVED
    @Operation(summary = "Get orders", tags = {"order", "get", "employee"})
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetails>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException{    
        pageNumber = pageNumber!=null?pageNumber:0;
        log.info("getOrders(username={}, page={}, type={})", authentication.getName(), pageNumber, orderStatus);
        // get order's status
        OrderStatus status = orderStatus!=null?OrderStatus.fromString(orderStatus.toUpperCase()):OrderStatus.All;
        // get orders
        Page<OrderDetails> orders = employeeOrderService.getOrdersByUsername(
            authentication.getName(), status, pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(orders);
    }

    // get order summary
    @Operation(summary = "Get order summary", tags = {"order", "get", "employee"},
        description = "Get number of pending orders")
    @GetMapping("/summary")
    public ResponseEntity<OrderSummary> getSummaryOrders(){
        OrderSummary summary = orderService.getOrderSummary();
        return ResponseEntity.ok().body(summary);
    }
}
