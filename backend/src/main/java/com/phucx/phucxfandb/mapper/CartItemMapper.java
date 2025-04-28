package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestCartItemDTO;
import com.phucx.phucxfandb.entity.Cart;
import com.phucx.phucxfandb.entity.CartItem;
import com.phucx.phucxfandb.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", source = "cart")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", source = "product.unitPrice")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CartItem toCartItem(RequestCartItemDTO requestCartItemDTO, Product product, Cart cart);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", source = "product.unitPrice")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateCartItem(RequestCartItemDTO requestCartItemDTO, Product product, @MappingTarget CartItem cartItem);
}
