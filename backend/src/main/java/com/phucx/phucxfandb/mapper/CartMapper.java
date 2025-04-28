package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.response.CartDTO;
import com.phucx.phucxfandb.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);

    
}
