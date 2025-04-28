package com.phucx.phucxfandb.service.customer;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;

public interface CustomerUpdateService {
    CustomerDTO updateCustomer(String customerId, RequestCustomerDTO requestCustomerDTO);
    CustomerDTO updateCustomerByUsername(String username, RequestCustomerDTO requestCustomerDTO);
}
