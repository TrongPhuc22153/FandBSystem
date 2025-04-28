package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/dine-in", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dine-in Order API", description = "Endpoints for managing dine-in table operations")
public class DineInOrderController {

    private final ReservationTableUpdateService reservationTableUpdateService;
    private final ReservationTableReaderService reservationTableReaderService;
    private final OrderProcessingService orderProcessingService;
    private final OrderReaderService orderReaderService;

    @GetMapping("/orders")
    @Operation(summary = "Get dine in orders", description = "Retrieves a list of all currently occupied tables.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved occupied tables"),
    })
    public ResponseEntity<Page<OrderDTO>> getDineInOrders(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<OrderDTO> occupiedTables = orderReaderService.getOrders(pageNumber, pageSize);
        return new ResponseEntity<>(occupiedTables, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new order", description = "Allows authorized users to create a new dine in order.")
    public ResponseEntity<ResponseDTO<OrderDTO>> createDineInOrder(
            Principal principal,
            @Valid @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        requestOrderDTO.setType(OrderType.DINE_IN);
        log.info("createMenuItem(requestOrderDTO={})", requestOrderDTO);
        OrderDTO orderDTO = orderProcessingService.placeOrderByEmployee(
                principal.getName(), requestOrderDTO);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order created successfully")
                .data(orderDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping
    @Operation(summary = "Seat a dine-in customer", description = "Assigns an available table to a dine-in customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table assigned successfully"),
    })
    public ResponseEntity<ResponseDTO<ReservationTableDTO>> seatDineInCustomer(
            @Parameter(description = "Number of guests to seat", required = true) @RequestParam int numberOfGuests) {
        ReservationTableDTO assignedTable = reservationTableUpdateService.seatDineInCustomer(numberOfGuests);
        ResponseDTO<ReservationTableDTO> response = ResponseDTO.<ReservationTableDTO>builder()
                .message("Table assigned successfully")
                .data(assignedTable)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{tableId}/available")
    @Operation(summary = "Mark a table as available", description = "Sets the status of a table to AVAILABLE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table status updated successfully"),
    })
    public ResponseEntity<ResponseDTO<Void>> markTableAvailable(
            @Parameter(description = "ID of the table to mark as available", required = true) @PathVariable String tableId) {
        reservationTableUpdateService.updateTableStatus(tableId, TableStatus.UNOCCUPIED);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Table status updated successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{tableId}/occupied")
    @Operation(summary = "Mark a table as occupied", description = "Sets the status of a table to OCCUPIED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table status updated successfully"),
    })
    public ResponseEntity<ResponseDTO<Void>> markTableOccupied(
            @Parameter(description = "ID of the table to mark as occupied", required = true) @PathVariable String tableId) {
        reservationTableUpdateService.updateTableStatus(tableId, TableStatus.OCCUPIED);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Table status updated successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tables")
    @Operation(summary = "Get tables", description = "Retrieves a list of all currently tables.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tables"),
    })
    public ResponseEntity<List<ReservationTableDTO>> getTables(@RequestParam(name = "status", defaultValue = "UNOCCUPIED") TableStatus status) {
        List<ReservationTableDTO> occupiedTables = reservationTableReaderService
                .getTables(status);
        return new ResponseEntity<>(occupiedTables, HttpStatus.OK);
    }

    @PostMapping("/tables/{tableId}/order/{orderId}/complete")
    @Operation(summary = "Cleanup and mark a table as done", description = "Allows staff to cleanup a table, mark it as done, and complete the associated order.")
    public ResponseEntity<ResponseDTO<OrderDTO>> cleanupAndMarkTableAsDone(
            @PathVariable String tableId,
            @PathVariable String orderId
    ) {
        log.info("cleanupAndMarkTableAsDone(tableId={}, orderId={})", tableId, orderId);
        OrderDTO orderDTO = orderProcessingService.completeOrder(tableId, orderId, OrderType.DINE_IN);

        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Table cleaned up, order completed, and table marked as done")
                .data(orderDTO)
                .build();
        return ResponseEntity.ok(response);
    }
}


