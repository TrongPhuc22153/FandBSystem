package com.phucx.phucxfandb.service.cart.impl;

import com.phucx.phucxfandb.annotation.EnsureCartExists;
import com.phucx.phucxfandb.dto.response.CartDTO;
import com.phucx.phucxfandb.entity.Cart;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CartMapper;
import com.phucx.phucxfandb.repository.CartRepository;
import com.phucx.phucxfandb.service.cart.CartReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartReaderServiceImpl implements CartReaderService {
    private final CartRepository cartRepository;
    private final CartMapper mapper;

    @Override
    public CartDTO getCart(String cartId) {
        log.info("getCart(cartId={})", cartId);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new NotFoundException("Cart", cartId));
        return mapper.toCartDTO(cart);
    }

    @Override
    @EnsureCartExists
    public CartDTO getCartByUsername(String username) {
        log.info("getCartByUsername(username={})", username);
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException("Cart", username));
        return mapper.toCartDTO(cart);
    }
}
