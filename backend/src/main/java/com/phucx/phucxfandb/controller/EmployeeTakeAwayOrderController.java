package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/employees/order")
@RequiredArgsConstructor
@Tag(name = "Employee Take-Away Order API", description = "Endpoints for managing employee orders")
public class EmployeeTakeAwayOrderController {
    private final OrderReaderService orderReaderService;
    private final OrderProcessingService orderProcessingService;

    @GetMapping("/{orderId}")
    @Operation(summary = "Get an employee order by ID", description = "Retrieves a single employee order by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getEmployeeOrderById(
            @Parameter(description = "ID of the employee order to retrieve", required = true) @PathVariable String orderId) {
        OrderDTO order = orderReaderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/cancel")
    @Operation(summary = "Cancel an order", description = "Cancels that an order has been received and is being processed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ResponseDTO<Void>> cancelOrder(
            Principal principal,
            @Parameter(description = "ID of the order to cancel", required = true) @PathVariable String orderId) {
        orderProcessingService.cancelOrderByEmployee(principal.getName(), orderId, OrderType.TAKE_AWAY);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Order cancel successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{orderId}/confirm")
    @Operation(summary = "Confirm an order", description = "Confirms that an order has been received and is being processed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ResponseDTO<OrderDTO>> confirmOrder(
            Principal principal,
            @Parameter(description = "ID of the order to confirm", required = true) @PathVariable String orderId) {
        OrderDTO confirmedOrder = orderProcessingService
                .confirmOrder(principal.getName(), orderId, OrderType.TAKE_AWAY);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order confirmed successfully")
                .data(confirmedOrder)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/complete")
    @Operation(summary = "Complete an order", description = "Confirms that an order has been received and is being processed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ResponseDTO<OrderDTO>> completeOrder(
            Principal principal,
            @Parameter(description = "ID of the order to confirm", required = true) @PathVariable String orderId) {
        OrderDTO completeOrder = orderProcessingService
                .completeTakeAwayOrder(principal.getName(), orderId, OrderType.TAKE_AWAY);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order received successfully")
                .data(completeOrder)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
