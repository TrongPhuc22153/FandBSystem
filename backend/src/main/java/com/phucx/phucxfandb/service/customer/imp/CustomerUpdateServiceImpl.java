package com.phucx.phucxfandb.service.customer.imp;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CustomerMapper;
import com.phucx.phucxfandb.repository.CustomerRepository;
import com.phucx.phucxfandb.service.customer.CustomerUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerUpdateServiceImpl implements CustomerUpdateService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    @Modifying
    @Transactional
    public CustomerDTO updateCustomer(String customerId, RequestCustomerDTO requestCustomerDTO) {
        log.info("updateCustomer(id={}, requestCustomerDTO={})", customerId, requestCustomerDTO);
        Customer existingCustomer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer", customerId));
        mapper.updateCustomer(requestCustomerDTO, existingCustomer);
        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapper.toCustomerDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomerByUsername(String username, RequestCustomerDTO requestCustomerDTO) {
        log.info("updateCustomerByUsername(username={}, requestCustomerDTO={})", username, requestCustomerDTO);
        Customer existingCustomer = customerRepository.findByProfileUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Customer", username));
        mapper.updateCustomer(requestCustomerDTO, existingCustomer);
        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapper.toCustomerDTO(updatedCustomer);
    }
}
