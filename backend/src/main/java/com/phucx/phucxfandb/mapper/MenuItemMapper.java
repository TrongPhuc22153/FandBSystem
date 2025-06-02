package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.response.MenuItemDTO;
import com.phucx.phucxfandb.entity.MenuItem;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MenuItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "reservation", source = "reservation")
    @Mapping(target = "price", source = "product.unitPrice")
    @Mapping(target = "status", constant = "PENDING")
    MenuItem toMenuItem(RequestMenuItemDTO requestMenuItemDTO, Reservation reservation, Product product);

    @Named("toMenuItemDTO")
    @Mapping(target = "product", qualifiedByName = {"toProductKitchen"})
    MenuItemDTO toMenuItemDTO(MenuItem menuItem);
}
