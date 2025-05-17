package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.dto.request.RequestOrderDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    private final OrderProcessingService orderProcessingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new dine-in order", description = "Allows authorized users to create a new dine in order.")
    public ResponseEntity<ResponseDTO<OrderDTO>> createDineInOrder(
            Principal principal,
            @Valid @RequestBody RequestOrderDTO requestOrderDTO
    ) {
        requestOrderDTO.setType(OrderType.DINE_IN);
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
}


