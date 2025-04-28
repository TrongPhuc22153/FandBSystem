package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toCustomerDTO(Customer customer);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    void updateCustomer(RequestCustomerDTO requestCustomerDTO, @MappingTarget Customer customer);
}
