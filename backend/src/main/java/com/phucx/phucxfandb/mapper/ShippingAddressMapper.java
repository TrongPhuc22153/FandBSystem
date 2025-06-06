package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestShippingAddressDTO;
import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {

    ShippingAddressDTO toShippingAddressDTO(ShippingAddress shippingAddress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", source = "requestShippingAddressDTO.phone")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "isDeleted", constant = "false")
    ShippingAddress toShippingAddress(RequestShippingAddressDTO requestShippingAddressDTO, Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateShippingAddressFromDTO(RequestShippingAddressDTO requestShippingAddressDTO, @MappingTarget ShippingAddress shippingAddress);

}
