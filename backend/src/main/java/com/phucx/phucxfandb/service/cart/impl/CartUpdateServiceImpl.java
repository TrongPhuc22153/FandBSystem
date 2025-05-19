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
    @Transactional
    public CartDTO updateCartItemQuantity(String username, RequestCartItemDTO requestCartItemDTO) {
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));
        Product product = productReaderService.getProductEntity(requestCartItemDTO.getProductId());

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item", "username", username));

        cartItemMapper.updateCartItem(requestCartItemDTO, product, existingItem);
        if(existingItem.getQuantity()>product.getUnitsInStock()){
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Transactional
    @EnsureCartExists
    public CartDTO updateCart(String username, RequestCartDTO requestCartDTO) {
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));
        existingCart.getCartItems().clear();

        List<CartItem> newItems = requestCartDTO.getCartItems().stream()
                .map(dto -> {
                    Product existingProduct = productReaderService.getProductEntity(dto.getProductId());
                    return cartItemMapper.toCartItem(dto, existingProduct, existingCart);
                })
                .collect(Collectors.toList());

        existingCart.setCartItems(newItems);
        existingCart.setTotalPrice(calculateTotalPrice(existingCart.getCartItems()));

        Cart updatedCart = cartRepository.save(existingCart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Transactional
    @EnsureCartExists
    public CartDTO addCartItem(String username, RequestCartItemDTO requestCartItemDTO) {
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));
        Product product = productReaderService.getProductEntity(requestCartItemDTO.getProductId());

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        int requestedQuantity = requestCartItemDTO.getQuantity();

        if (existingItem != null) {
            int newTotalQuantity = existingItem.getQuantity() + requestedQuantity;
            if (newTotalQuantity > product.getUnitsInStock()) {
                throw new IllegalArgumentException("Total requested quantity exceeds available stock");
            }
            existingItem.setQuantity(newTotalQuantity);
            existingItem.setUnitPrice(product.getUnitPrice());
        } else {
            if (requestedQuantity > product.getUnitsInStock()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            }
            CartItem newCartItem = cartItemMapper.toCartItem(requestCartItemDTO, product, cart);
            cart.getCartItems().add(newCartItem);
        }

        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartDTO(updatedCart);
    }

    @Override
    @Transactional
    public void removeCartItems(String username, List<Long> productIds) {
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));

        List<CartItem> cartItemsToRemove = existingCart.getCartItems().stream()
                .filter(item -> productIds.contains(item.getProduct().getProductId()))
                .toList();

        existingCart.getCartItems().removeAll(cartItemsToRemove);
        cartRepository.save(existingCart);
    }



    @Override
    @Transactional
    public void removeCartItems(String username) {
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));
        existingCart.getCartItems().clear();
        existingCart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public CartDTO removeCartItem(String username, long productId) {
        Cart existingCart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "username", username));
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
