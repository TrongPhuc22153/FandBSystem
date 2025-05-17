package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.OrderRequestParamDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Order API", description = "Order operation endpoints")
public class OrderController {
    private final OrderReaderService orderReaderService;
    private final OrderProcessingService orderProcessingService;

    @GetMapping
    @Operation(summary = "Get orders", description = "Authenticated access")
    public ResponseEntity<Page<OrderDTO>> getOrders(
            Authentication authentication,
            @ModelAttribute OrderRequestParamDTO params
    ) {
        Page<OrderDTO> orders = orderReaderService.getOrders(params, authentication);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order", description = "Authenticated access")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable(name = "orderId") String orderId
    ) {
        OrderDTO order = orderReaderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Process order", description = "Employee access")
    public ResponseEntity<ResponseDTO<OrderDTO>> updateOrderStatus(
            Authentication authentication,
            @PathVariable String orderId,
            @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        OrderDTO updatedOrder = orderProcessingService.processOrder(
                authentication,
                orderId,
                requestOrderDTO.getAction(),
                requestOrderDTO.getType());
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order updated successfully")
                .data(updatedOrder)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new order", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<OrderDTO>> createOrder(
            Authentication authentication,
            @Valid @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        OrderDTO orderDTO = orderProcessingService.placeOrder(requestOrderDTO, authentication);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order placed successfully")
                .data(orderDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
