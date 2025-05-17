package com.phucx.phucxfandb.service.address.impl;

import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;
import com.phucx.phucxfandb.entity.ShippingAddress;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ShippingAddressMapper;
import com.phucx.phucxfandb.repository.ShippingAddressRepository;
import com.phucx.phucxfandb.service.address.ShippingAddressReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingAddressReaderServiceImpl implements ShippingAddressReaderService {
    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ShippingAddress getShippingAddressEntity(long id) {
        return shippingAddressRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new NotFoundException(ShippingAddress.class.getName(), id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingAddressDTO> getShippingAddressByUsername(String username) {
        return shippingAddressRepository.findByCustomerProfileUserUsernameAndIsDeletedFalse(username).stream()
                .map(mapper::toShippingAddressDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCustomerId(String customerId) {
        return shippingAddressRepository.existsByCustomerCustomerIdAndIsDeletedFalse(customerId);
    }
}
