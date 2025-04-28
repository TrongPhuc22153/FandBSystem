package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestCartItemDTO;
import com.phucx.phucxfandb.dto.response.CartDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.cart.CartReaderService;
import com.phucx.phucxfandb.service.cart.CartUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Cart", description = "User cart endpoint")
public class CartController {
    private final CartReaderService cartReaderService;
    private final CartUpdateService cartUpdateService;

    @GetMapping("/me")
    @Operation(summary = "Get user cart", description = "Customer access")
    public ResponseEntity<CartDTO> getUserCart(Principal principal){
        log.info("getUserCart(username={})", principal.getName());
        CartDTO cartDTO = cartReaderService.getCartByUsername(principal.getName());
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping(value = "/me/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add cart item quantity in user cart", description = "Customer access")
    public ResponseEntity<ResponseDTO<CartDTO>> addCartItem(
            @Valid @RequestBody RequestCartItemDTO requestCartItemDTO,
            Principal principal
    ){
        log.info("addCartItem(username={}, requestCartItemDTO={})", principal.getName(), requestCartItemDTO);
        CartDTO cartDTO = cartUpdateService.addCartItem(principal.getName(), requestCartItemDTO);
        ResponseDTO<CartDTO> response = ResponseDTO.<CartDTO>builder()
                .message("Cart item quantity updated successfully")
                .data(cartDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/me/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update cart item quantity in user cart", description = "Customer access")
    public ResponseEntity<ResponseDTO<CartDTO>> updateCartItemQuantity(
            @Valid @RequestBody RequestCartItemDTO requestCartItemDTO,
            Principal principal
    ){
        log.info("updateCartItemQuantity(username={}, requestCartItemDTO={})", principal.getName(), requestCartItemDTO);
        CartDTO cartDTO = cartUpdateService.updateCartItemQuantity(principal.getName(), requestCartItemDTO);
        ResponseDTO<CartDTO> response = ResponseDTO.<CartDTO>builder()
                .message("Cart item quantity updated successfully")
                .data(cartDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/items/{productId}")
    @Operation(summary = "Remove item from user cart", description = "Customer access")
    public ResponseEntity<ResponseDTO<CartDTO>> removeItemFromUserCart(
            Principal principal,
            @PathVariable long productId) {
        log.info("removeItemFromUserCart(username={}, productId={})", principal.getName(), productId);
        CartDTO updatedCartDTO = cartUpdateService.removeCartItem(principal.getName(), productId);
        ResponseDTO<CartDTO> responseDTO = ResponseDTO.<CartDTO>builder()
                .message("Item removed from cart successfully")
                .data(updatedCartDTO)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Clear user cart", description = "Customer access")
    public ResponseEntity<ResponseDTO<Void>> clearCart(Principal principal) {
        log.info("clearCart(username={})", principal.getName());
        cartUpdateService.removeCartItems(principal.getName());
        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                .message("Cart cleared successfully")
                .build();
        return ResponseEntity.ok(responseDTO);
    }


}
