package com.phucx.phucxfandb.service.address.impl;

import com.phucx.phucxfandb.dto.request.RequestShippingAddressDTO;
import com.phucx.phucxfandb.dto.response.ShippingAddressDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.ShippingAddress;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ShippingAddressMapper;
import com.phucx.phucxfandb.repository.ShippingAddressRepository;
import com.phucx.phucxfandb.service.address.ShippingAddressUpdateService;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingAddressUpdateServiceImpl implements ShippingAddressUpdateService {
    private final ShippingAddressRepository shippingAddressRepository;
    private final CustomerReaderService customerReaderService;
    private final ShippingAddressMapper mapper;

    @Override
    @Transactional
    public ShippingAddressDTO updateShippingAddress(long id, RequestShippingAddressDTO requestShippingAddressDTO) {
        ShippingAddress existingShippingAddress = shippingAddressRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new NotFoundException(ShippingAddress.class.getSimpleName(), id));

        Customer customer = existingShippingAddress.getCustomer();

        if (Boolean.TRUE.equals(requestShippingAddressDTO.getIsDefault())) {
            customer.getShippingAddresses().forEach(shippingAddress -> shippingAddress
                    .setIsDefault(Boolean.FALSE));
            shippingAddressRepository.saveAll(customer.getShippingAddresses());
        }

        mapper.updateShippingAddressFromDTO(requestShippingAddressDTO, existingShippingAddress);
        ShippingAddress saved = shippingAddressRepository.save(existingShippingAddress);
        return mapper.toShippingAddressDTO(saved);
    }

    @Override
    @Transactional
    public ShippingAddressDTO updateShippingAddressByUsername(String username, long shippingId, RequestShippingAddressDTO requestShippingAddressDTO) {
        ShippingAddress existingShippingAddress = shippingAddressRepository.findByIdAndIsDeletedFalse(shippingId)
                .orElseThrow(()-> new NotFoundException(ShippingAddress.class.getSimpleName(), shippingId));

        Customer customer = existingShippingAddress.getCustomer();

        if(!customer.getProfile().getUser().getUsername().equalsIgnoreCase(username)){
            throw new AccessDeniedException("User is not authorized to access this resource");
        }

        if (Boolean.TRUE.equals(requestShippingAddressDTO.getIsDefault())) {
            customer.getShippingAddresses().forEach(shippingAddress -> shippingAddress
                    .setIsDefault(Boolean.FALSE));
            shippingAddressRepository.saveAll(customer.getShippingAddresses());
        }

        mapper.updateShippingAddressFromDTO(requestShippingAddressDTO, existingShippingAddress);
        ShippingAddress saved = shippingAddressRepository.save(existingShippingAddress);
        return mapper.toShippingAddressDTO(saved);
    }

    @Override
    @Transactional
    public ShippingAddressDTO createShippingAddress(RequestShippingAddressDTO requestShippingAddressDTO) {
        Customer customer = customerReaderService.getCustomerEntityById(
                requestShippingAddressDTO.getCustomerId()
        );

        if (Boolean.TRUE.equals(requestShippingAddressDTO.getIsDefault())) {
            customer.getShippingAddresses().forEach(shippingAddress -> shippingAddress
                    .setIsDefault(Boolean.FALSE));
            shippingAddressRepository.saveAll(customer.getShippingAddresses());
        }
        ShippingAddress newShippingAddress = mapper.toShippingAddress(requestShippingAddressDTO, customer);

        ShippingAddress savedShippingAddress = shippingAddressRepository.save(newShippingAddress);
        return mapper.toShippingAddressDTO(savedShippingAddress);
    }

    @Override
    @Transactional
    public ShippingAddressDTO createShippingAddressByUsername(String username, RequestShippingAddressDTO requestShippingAddressDTO) {
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);

        if (Boolean.TRUE.equals(requestShippingAddressDTO.getIsDefault())) {
            customer.getShippingAddresses().forEach(shippingAddress -> shippingAddress
                    .setIsDefault(Boolean.FALSE));
            shippingAddressRepository.saveAll(customer.getShippingAddresses());
        }
        ShippingAddress newShippingAddress = mapper.toShippingAddress(requestShippingAddressDTO, customer);

        ShippingAddress savedShippingAddress = shippingAddressRepository.save(newShippingAddress);
        return mapper.toShippingAddressDTO(savedShippingAddress);
    }

    @Override
    @Transactional
    public void deleteShippingAddress(String username, long id) {
        ShippingAddress existingShippingAddress = shippingAddressRepository
            .findByCustomerProfileUserUsernameAndIdAndIsDeletedFalse(username, id)
                .orElseThrow(()-> new NotFoundException(
                        ShippingAddress.class.getSimpleName(),
                        "id",
                        String.valueOf(id)));
        existingShippingAddress.setIsDeleted(true);
        shippingAddressRepository.save(existingShippingAddress);
    }
}
