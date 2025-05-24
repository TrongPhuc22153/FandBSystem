package com.phucx.phucxfandb.service.cart.impl;

import com.phucx.phucxfandb.annotation.EnsureCartExists;
import com.phucx.phucxfandb.dto.response.CartDTO;
import com.phucx.phucxfandb.entity.Cart;
import com.phucx.phucxfandb.entity.CartItem;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CartMapper;
import com.phucx.phucxfandb.repository.CartRepository;
import com.phucx.phucxfandb.service.cart.CartReaderService;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartReaderServiceImpl implements CartReaderService {
    private final CartRepository cartRepository;
    private final ImageReaderService imageReaderService;
    private final CartMapper mapper;

    @Override
    @EnsureCartExists
    public CartDTO getCartByUsername(String username) {
        Cart cart = cartRepository.findByCustomerProfileUserUsername(username)
                .orElseThrow(()-> new NotFoundException(Cart.class.getSimpleName(), "user", username));
        cart.getCartItems().forEach(this::setImageUrl);
        return mapper.toCartDTO(cart);
    }

    private void setImageUrl(CartItem cartItem){
        if(!(cartItem.getProduct().getPicture()==null || cartItem.getProduct().getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(cartItem.getProduct().getPicture());
            cartItem.getProduct().setPicture(imageUrl);
        }
    }
}
