package com.phucx.phucxfandb.service.customer;

import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;

public interface CustomerReaderService {
    // get customer
    CustomerDTO getCustomerById(String customerID);
    CustomerDTO getCustomerByUsername(String username);
    Customer getCustomerEntityById(String customerID);
    Customer getCustomerEntityByUsername(String username);
}
