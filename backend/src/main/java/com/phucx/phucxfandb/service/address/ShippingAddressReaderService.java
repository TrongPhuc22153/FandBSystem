package com.phucx.phucxfandb.service.address;

import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;
import com.phucx.phucxfandb.entity.ShippingAddress;

import java.util.List;

public interface ShippingAddressReaderService {
    ShippingAddressDTO getShippingAddress(long id);
    ShippingAddress getShippingAddressEntity(long id);
    List<ShippingAddressDTO> getShippingAddressByUsername(String username);

    boolean existsByCustomerId(String customerId);
}
