package com.phucx.phucxfoodshop.controller;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.CookieConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.CartOrderInfo;
import com.phucx.phucxfoodshop.model.dto.CartProduct;
import com.phucx.phucxfoodshop.model.dto.CartProductsCookie;
import com.phucx.phucxfoodshop.model.dto.OrderWithProducts;
import com.phucx.phucxfoodshop.service.cart.CartService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/shop/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Update cart", 
        tags = {"cart", "post", "customer"},
        description = "Update cart's products")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> updateCartCookie(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        CartOrderInfo cartOrder = cartService.updateCartCookie(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @Operation(summary = "Add product to cart", tags = {"cart", "post", "customer"},
        description = "Increate or decreate products's quantity or add a new product to cart")
    @PostMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> addProduct(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        CartOrderInfo cartOrder = cartService.addProduct(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @Operation(summary = "Remove some products from cart", tags = {"cart", "post", "customer"})
    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> removeCartProductsCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @RequestBody List<Integer> productIDs
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo cartOrder = cartService.removeProducts(productIDs, cartJson, response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Remove product from cart", tags = {"cart", "delete", "customer"})
    @DeleteMapping(value = "/product/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> deleteCartProductCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @PathVariable("productID") Integer productID
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo cartOrder = cartService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Remove all products in cart", tags = {"cart", "delete", "customer"})
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> deleteCartProductsCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartOrderInfo cartOrder = cartService.removeProducts(response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Get products in cart", tags = {"cart", "get", "customer"})
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartOrderInfo> getOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo order = cartService.getCartProducts(cartJson);
        return ResponseEntity.ok().body(order);
    }

    @Operation(summary = "Get number of products in cart", tags = {"cart", "get", "customer"})
    @GetMapping(value = "/products/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartProductsCookie> getNumberOfOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartProductsCookie cartProductsCookie = cartService.getNumberOfProducts(cartJson);
        return ResponseEntity.ok().body(cartProductsCookie);
    }

    @Operation(summary = "Get order of selected products in cart", tags = {"cart", "get", "customer"},
        description = "Get an order based on the seletecd products")
    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderWithProducts> getOrder(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException, CustomerNotFoundException{
        OrderWithProducts order = cartService.getOrder(cartJson, authentication.getName());
        return ResponseEntity.ok().body(order);
    }
}
