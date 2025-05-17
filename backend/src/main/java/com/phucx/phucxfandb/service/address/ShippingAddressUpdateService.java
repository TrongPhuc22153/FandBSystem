package com.phucx.phucxfandb.service.address;


import com.phucx.phucxfandb.dto.request.RequestShippingAddressDTO;
import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;

public interface ShippingAddressUpdateService {
    ShippingAddressDTO updateShippingAddress(long id, RequestShippingAddressDTO requestShippingAddressDTO);
    ShippingAddressDTO updateShippingAddressByUsername(String username, long id, RequestShippingAddressDTO requestShippingAddressDTO);
    ShippingAddressDTO createShippingAddress(RequestShippingAddressDTO shippingAddressDTO);
    ShippingAddressDTO createShippingAddressByUsername(String username, RequestShippingAddressDTO shippingAddressDTO);
    void deleteShippingAddress(String username, long id);
}
