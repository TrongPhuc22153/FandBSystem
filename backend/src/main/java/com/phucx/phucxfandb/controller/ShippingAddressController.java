package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestShippingAddressDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;
import com.phucx.phucxfandb.service.address.ShippingAddressReaderService;
import com.phucx.phucxfandb.service.address.ShippingAddressUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Shipping Address API", description = "Customer operations for shipping addresses")
@RequestMapping(value = "/api/v1/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShippingAddressController {
    private final ShippingAddressUpdateService shippingAddressUpdateService;
    private final ShippingAddressReaderService shippingAddressReaderService;

    @GetMapping("/me")
    @Operation(summary = "Get authenticated user's shipping address", description = "Customer access")
    public ResponseEntity<List<ShippingAddressDTO>> getShippingAddresses(Principal principal) {
        log.info("getShippingAddress(username={})", principal.getName());
        var shippingAddressDTO = shippingAddressReaderService.getShippingAddressByUsername(principal.getName());
        return ResponseEntity.ok(shippingAddressDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update authenticated user's shipping address", description = "Customer access")
    public ResponseEntity<ResponseDTO<ShippingAddressDTO>> updateShippingAddress(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody RequestShippingAddressDTO requestShippingAddressDTO) {
        log.info("updateShippingAddress(username={}, shippingAddressId={}, requestShippingAddressDTO={})", principal.getName(), id, requestShippingAddressDTO);
        var shippingAddressDTO = shippingAddressUpdateService.updateShippingAddressByUsername(principal.getName(), id, requestShippingAddressDTO);

        ResponseDTO<ShippingAddressDTO> responseDTO = ResponseDTO.<ShippingAddressDTO>builder()
                .message("Your shipping address updated successfully")
                .data(shippingAddressDTO)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create authenticated user's shipping address", description = "Customer access")
    public ResponseEntity<ResponseDTO<ShippingAddressDTO>> createShippingAddress(
            Principal principal,
            @Valid @RequestBody RequestShippingAddressDTO requestShippingAddressDTO) {
        log.info("createShippingAddress(username={}, requestShippingAddressDTO={})", principal.getName(), requestShippingAddressDTO);
        var shippingAddressDTO = shippingAddressUpdateService.createShippingAddressByUsername(principal.getName(), requestShippingAddressDTO);

        ResponseDTO<ShippingAddressDTO> responseDTO = ResponseDTO.<ShippingAddressDTO>builder()
                .message("Your shipping address created successfully")
                .data(shippingAddressDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete authenticated user's shipping address", description = "Customer access")
    public ResponseEntity<ResponseDTO<Void>> deleteShippingAddress(
            Principal principal,
            @PathVariable long id) {
        log.info("deleteShippingAddress(username={}, id={})", principal.getName(), id);
        shippingAddressUpdateService.deleteShippingAddress(principal.getName(), id);

        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                .message("Your shipping address deleted successfully")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

}
