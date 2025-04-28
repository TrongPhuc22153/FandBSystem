package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.response.MenuItemDTO;
import com.phucx.phucxfandb.entity.MenuItem;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reservation", source = "reservation")
    MenuItem toMenuItem(RequestMenuItemDTO requestMenuItemDTO, Reservation reservation, Product product);

    MenuItemDTO toMenuItemDTO(MenuItem menuItem);
}
