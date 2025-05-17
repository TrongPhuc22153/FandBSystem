package com.phucx.phucxfandb.service.customer.imp;

import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CustomerMapper;
import com.phucx.phucxfandb.repository.CustomerRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerReaderServiceImp implements CustomerReaderService {
    private final CustomerRepository customerRepository;
    private final ImageReaderService imageReaderService;
    private final CustomerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(String customerId){
        return customerRepository.findById(customerId)
                .map(this::setImageUrl)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new NotFoundException(Customer.class.getName(), customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByUsername(String username) {
        return customerRepository.findByProfileUserUsername(username)
                .map(this::setImageUrl)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new NotFoundException(Customer.class.getName(), username));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerEntityById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(Customer.class.getName(), customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerEntityByUsername(String username) {
        return customerRepository.findByProfileUserUsername(username)
                .orElseThrow(() -> new NotFoundException(Customer.class.getName(), username));
    }

    private Customer setImageUrl(Customer customer){
        if(!(customer.getProfile().getPicture()==null || customer.getProfile().getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(customer.getProfile().getPicture());
            customer.getProfile().setPicture(imageUrl);
        }
        return customer;
    }

}
