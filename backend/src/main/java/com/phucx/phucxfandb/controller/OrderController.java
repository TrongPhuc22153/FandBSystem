package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.dto.request.OrderRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderDetailService;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.order.OrderUpdateService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Order API", description = "Order operation endpoints")
public class OrderController {
    private final OrderReaderService orderReaderService;
    private final OrderUpdateService orderUpdateService;
    private final OrderDetailService orderDetailService;
    private final OrderProcessingService orderProcessingService;

    @GetMapping
    @Operation(summary = "Get orders", description = "Authenticated access")
    public ResponseEntity<Page<OrderDTO>> getOrders(
            Authentication authentication,
            @ModelAttribute OrderRequestParamsDTO params
    ) {
        Page<OrderDTO> orders = orderReaderService.getOrders(params, authentication);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order", description = "Authenticated access")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable(name = "id") String id
    ) {
        OrderDTO order = orderReaderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Process order", description = "Employee access")
    public ResponseEntity<ResponseDTO<OrderDTO>> processOrder(
            Authentication authentication,
            @PathVariable String id,
            @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        OrderDTO updatedOrder = orderProcessingService.processOrder(
                authentication,
                id,
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
        OrderDTO order = orderProcessingService
                .placeOrder(requestOrderDTO, authentication);

        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order placed successfully")
                .data(order)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update order", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<OrderDTO>> updateOrder(
            @PathVariable String id,
            Authentication authentication,
            @Valid @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        OrderDTO order = orderUpdateService
                .updateOrder(authentication.getName(), id, requestOrderDTO);

        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order updated successfully")
                .data(order)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/{id}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update order item", description = "Employee access")
    public ResponseEntity<ResponseDTO<OrderDTO>> addOrderItem(
            @PathVariable String id,
            @RequestBody RequestOrderDetailsDTO request) {
        OrderDTO updatedOrder = orderDetailService.addOrderItem(id, request);

        ResponseDTO<OrderDTO> responseDTO = ResponseDTO.<OrderDTO>builder()
                .message("Order item added successfully")
                .data(updatedOrder)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "Update order item quantity", description = "Employee access")
    public ResponseEntity<ResponseDTO<OrderDTO>> updateOrderItemQuantity(
            @PathVariable String orderId,
            @PathVariable String orderItemId,
            @RequestBody RequestOrderDetailsDTO request) {
        OrderDTO updatedOrder = orderDetailService.updateOrderItemQuantity(orderId, orderItemId, request);

        ResponseDTO<OrderDTO> responseDTO = ResponseDTO.<OrderDTO>builder()
                .message("Order item updated successfully")
                .data(updatedOrder)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "Update order item quantity", description = "Employee access")
    public ResponseEntity<ResponseDTO<OrderDTO>> updateOrderItemStatus(
            @PathVariable String orderId,
            @PathVariable String orderItemId,
            @Validated(ValidationGroups.UpdateOrderItemStatus.class)
            @RequestBody RequestOrderDetailsDTO request) {
        OrderDTO updatedOrder = orderDetailService.updateOrderItemStatus(orderId, orderItemId, request);

        ResponseDTO<OrderDTO> responseDTO = ResponseDTO.<OrderDTO>builder()
                .message("Order item status updated successfully")
                .data(updatedOrder)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "Cancel order item", description = "Employee access")
    public ResponseEntity<ResponseDTO<Void>> cancelOrderItem(
            @PathVariable String orderId,
            @PathVariable String orderItemId){
        orderDetailService.cancelOrderItem(orderId, orderItemId);
        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                .message("Order item cancelled successfully")
                .build();

        return ResponseEntity.ok(responseDTO);
    }
}
