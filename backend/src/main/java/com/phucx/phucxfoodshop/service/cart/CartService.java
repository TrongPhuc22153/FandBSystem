package com.phucx.phucxfoodshop.service.cart;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CartOrderInfo;
import com.phucx.phucxfoodshop.model.CartProduct;
import com.phucx.phucxfoodshop.model.CartProductsCookie;
import com.phucx.phucxfoodshop.model.OrderWithProducts;

import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
    // add product to cart
    public CartOrderInfo addProduct(String encodedCartJson, List<CartProduct> cartProducts, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException;
    // remove product from cart
    public CartOrderInfo removeProduct(Integer productID, String encodedCartJson, HttpServletResponse response) throws JsonProcessingException, NotFoundException;
    public CartOrderInfo removeProducts(HttpServletResponse response) throws JsonProcessingException;
    public CartOrderInfo removeProducts(List<Integer> productIDs, String encodedCartJson, HttpServletResponse response) throws JsonProcessingException, NotFoundException;
    // get order cart product
    public CartOrderInfo getCartProducts(String encodedCartJson) throws JsonProcessingException, NotFoundException;
    // get order for user to check out
    public OrderWithProducts getOrder(String encodedCartJson, String username) 
        throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException, CustomerNotFoundException;
    // update product in cart
    public CartOrderInfo updateCartCookie(String encodedCartJson, List<CartProduct> cartProducts, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException;
    // get number of products
        public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonProcessingException;
    // get list of product in cart
    public List<CartProduct> getListProducts(String encodedCartJson) throws JsonProcessingException;
}
