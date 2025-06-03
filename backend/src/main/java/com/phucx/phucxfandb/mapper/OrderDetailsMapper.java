package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestOrderDetailsDTO;
import com.phucx.phucxfandb.dto.response.OrderDetailDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderDetailsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", source = "product.unitPrice")
    @Mapping(target = "status", constant = "PENDING")
    OrderDetail toOrderDetail(RequestOrderDetailsDTO requestOrderDetailsDTO, Product product, Order order);

    @Named("toOrderDetailsDTO")
    @Mapping(target = "product", qualifiedByName = {"toProductKitchen"})
    OrderDetailDTO toOrderDetailsDTO(OrderDetail orderDetail);
}
