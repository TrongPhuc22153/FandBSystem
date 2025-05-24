package com.phucx.phucxfandb.service.cart;

import com.phucx.phucxfandb.dto.response.CartDTO;

public interface CartReaderService {
    CartDTO getCartByUsername(String username);
}
