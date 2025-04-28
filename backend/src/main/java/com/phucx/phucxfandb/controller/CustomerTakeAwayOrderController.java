package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Customer Take-Away Order API", description = "Customer take-away order endpoint")
@RequestMapping(value = "/api/v1/customers/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerTakeAwayOrderController {
    private final OrderReaderService orderReaderService;
    private final OrderProcessingService orderProcessingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Place an order", description = "Allows a customer to place a new order.")
    public ResponseEntity<ResponseDTO<OrderDTO>> placeOrderByCustomer(
            Principal principal,
            @Valid @RequestBody RequestOrderDTO requestOrderDTO) {
        requestOrderDTO.setType(OrderType.TAKE_AWAY);
        log.info("placeOrderByCustomer(username={}, requestOrderDTO={})", principal.getName(), requestOrderDTO);
        OrderDTO orderDTO = orderProcessingService.placeOrderByCustomer(principal.getName(), requestOrderDTO);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order placed successfully")
                .data(orderDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{orderId}/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cancel an order", description = "Allows a customer to cancel an order.")
    public ResponseEntity<ResponseDTO<Void>> cancelOrder(
            Principal principal,
            @PathVariable String orderId) {
        log.info("cancelPendingOrder(username={}, orderId={})", principal.getName(), orderId);
        orderProcessingService.cancelPendingOrder(principal.getName(), orderId, OrderType.TAKE_AWAY);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Order cancelled")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{orderId}/receive", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Receive a customer order", description = "Allows a customer to confirm receipt of their order.")
    public ResponseEntity<ResponseDTO<OrderDTO>> receiveCustomerOrder(
            Principal principal,
            @PathVariable String orderId) {
        log.info("receiveCustomerOrder(username={}, orderId={})", principal.getName(), orderId);
        OrderDTO orderDTO = orderProcessingService.receiveCustomerOrder(
                principal.getName(), orderId, OrderType.TAKE_AWAY);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order received")
                .data(orderDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get a customer order by ID", description = "Retrieves a single customer order by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getCustomerOrderById(
            @Parameter(description = "ID of the customer order to retrieve", required = true) @PathVariable String orderId) {
        OrderDTO order = orderReaderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

}
