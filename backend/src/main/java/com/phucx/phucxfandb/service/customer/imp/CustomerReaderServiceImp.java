package com.phucx.phucxfandb.service.customer.imp;

import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CustomerMapper;
import com.phucx.phucxfandb.repository.CustomerRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.image.CustomerImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerReaderServiceImp implements CustomerReaderService {
    private final CustomerRepository customerRepository;
    private final CustomerImageService customerImageService;
    private final CustomerMapper mapper;

    @Override
    public CustomerDTO getCustomerById(String customerId){
        log.info("getCustomerByID(customerId={})", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer", customerId));
        return mapper.toCustomerDTO(customerImageService.setCustomerImage(customer));
    }

    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        log.info("getCustomerByUsername(username={})", username);
        Customer customer = customerRepository.findByProfileUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Customer", username));
        return mapper.toCustomerDTO(customerImageService.setCustomerImage(customer));
    }

    @Override
    public Customer getCustomerEntityById(String customerId) {
        log.info("getCustomerEntityById(customerId={})", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer", customerId));
        return customerImageService.setCustomerImage(customer);
    }

    @Override
    public Customer getCustomerEntityByUsername(String username) {
        log.info("getCustomerEntityByUsername(username={})", username);
        return customerRepository.findByProfileUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Customer", username));
    }

}
