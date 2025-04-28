package com.phucx.phucxfandb.service.cart.impl;

import com.phucx.phucxfandb.annotation.EnsureCartExists;
import com.phucx.phucxfandb.dto.request.RequestCartDTO;
import com.phucx.phucxfandb.dto.request.RequestCartItemDTO;
import com.phucx.phucxfandb.dto.response.CartDTO;
import com.phucx.phucxfandb.entity.Cart;
import com.phucx.phucxfandb.entity.CartItem;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CartItemMapper;
import com.phucx.phucxfandb.mapper.CartMapper;
import com.phucx.phucxfandb.repository.CartRepository;
import com.phucx.phucxfandb.service.cart.CartUpdateService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartUpdateServiceImpl implements CartUpdateService {
    private final ProductReaderService productReaderService;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Modifying
    @Transactional
    @EnsureCartExists
    public CartDTO updateCartItemQuantity(String username, RequestCartItemDTO requestCartItemDTO) {
        log.info("updateCartItemQuantity(username={}, requestCartItemDTO={})", username, requestCartItemDTO);
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", "user", "username", username));
        Product product = productReaderService.getProductEntity(requestCartItemDTO.getProductId());

        // Check if the product already exists in the cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item", "user", "username", username));

        cartItemMapper.updateCartItem(requestCartItemDTO, product, existingItem);
        if(existingItem.getQuantity()>product.getUnitsInStock()){
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        // Update total price
        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Modifying
    @Transactional
    @EnsureCartExists
    public CartDTO updateCart(String username, RequestCartDTO requestCartDTO) {
        log.info("updateCart(username={}, requestCartDTO={})", username, requestCartDTO);
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", "user", "username", username));
        // Empty all existing cart items
        existingCart.getCartItems().clear();
        // Add new cart items
        List<CartItem> newItems = requestCartDTO.getCartItems().stream()
                .map(dto -> {
                    Product existingProduct = productReaderService.getProductEntity(dto.getProductId());
                    return cartItemMapper.toCartItem(dto, existingProduct, existingCart);
                })
                .collect(Collectors.toList());
        // Save new items
        existingCart.setCartItems(newItems);
        // Recalculate total price
        existingCart.setTotalPrice(calculateTotalPrice(existingCart.getCartItems()));
        // Save updated cart
        Cart updatedCart = cartRepository.save(existingCart);

        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Modifying
    @Transactional
    @EnsureCartExists
    public CartDTO addCartItem(String username, RequestCartItemDTO requestCartItemDTO) {
        log.info("addCartItem(username={}, requestCartItemDTO={})", username, requestCartItemDTO);
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", "user", "username", username));
        Product product = productReaderService.getProductEntity(requestCartItemDTO.getProductId());

        CartItem newCartItem = cartItemMapper.toCartItem(requestCartItemDTO, product, cart);
        if(newCartItem.getQuantity()>product.getUnitsInStock()){
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        // Check if the product already exists in the cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update existing item
            existingItem.setQuantity(newCartItem.getQuantity() + existingItem.getQuantity());
            existingItem.setUnitPrice(newCartItem.getUnitPrice());
        } else {
            // Add new item
            cart.getCartItems().add(newCartItem);
        }

        // Update total price
        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Modifying
    @Transactional
    @EnsureCartExists
    public CartDTO removeCartItems(String username) {
        log.info("removeCartItems(username={})", username);
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", "user", "username", username));
        existingCart.getCartItems().clear();
        existingCart.setTotalPrice(BigDecimal.ZERO);
        Cart updatedCart = cartRepository.save(existingCart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Modifying
    @Transactional
    @EnsureCartExists
    public CartDTO removeCartItem(String username, long productId) {
        log.info("removeCartItem(username={}, productId={})", username, productId);
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", "user", "username", username));
        CartItem cartItemToRemove = existingCart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new NotFoundException(String.format("Cart item of product id %d does not exist", productId)));
        existingCart.getCartItems().remove(cartItemToRemove);
        existingCart.setTotalPrice(calculateTotalPrice(existingCart.getCartItems()));
        Cart updatedCart = cartRepository.save(existingCart);
        return cartMapper.toCartDTO(updatedCart);
    }

    private BigDecimal calculateTotalPrice(List<CartItem> cartItems){
        return cartItems.stream()
                .map(item -> item
                        .getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
