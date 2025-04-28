package com.phucx.phucxfandb.service.cart;

import com.phucx.phucxfandb.dto.request.RequestCartDTO;
import com.phucx.phucxfandb.dto.request.RequestCartItemDTO;
import com.phucx.phucxfandb.dto.response.CartDTO;

public interface CartUpdateService {
    CartDTO updateCartItemQuantity(String username, RequestCartItemDTO requestCartItemDTO);
    CartDTO updateCart(String username, RequestCartDTO requestCartDTO);
    CartDTO addCartItem(String username, RequestCartItemDTO requestCartItemDTO);
    CartDTO removeCartItems(String userId);
    CartDTO removeCartItem(String username, long productId);
}
