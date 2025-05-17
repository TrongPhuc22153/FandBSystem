package com.phucx.phucxfandb.service.customer.imp;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.request.RequestShippingAddressDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CustomerMapper;
import com.phucx.phucxfandb.repository.CustomerRepository;
import com.phucx.phucxfandb.service.address.ShippingAddressReaderService;
import com.phucx.phucxfandb.service.address.ShippingAddressUpdateService;
import com.phucx.phucxfandb.service.customer.CustomerUpdateService;
import com.phucx.phucxfandb.service.user.UserProfileUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerUpdateServiceImpl implements CustomerUpdateService {
    private final ShippingAddressUpdateService shippingAddressUpdateService;
    private final ShippingAddressReaderService shippingAddressReaderService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    @Transactional
    public CustomerDTO updateCustomerByUsername(String username, RequestCustomerDTO requestCustomerDTO) {
        Customer existingCustomer = customerRepository.findByProfileUserUsername(username)
                .orElseThrow(() -> new NotFoundException(Customer.class.getName(), username));

        userProfileUpdateService.updateUserProfile(username, requestCustomerDTO.getProfile());

        if(!shippingAddressReaderService.existsByCustomerId(existingCustomer.getCustomerId())){
            createNewShippingAddress(existingCustomer);
        }

        mapper.updateCustomer(requestCustomerDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapper.toCustomerDTO(updatedCustomer);
    }

    private void createNewShippingAddress(Customer customer){
        UserProfile profile = customer.getProfile();

        RequestShippingAddressDTO requestShippingAddressDTO = new RequestShippingAddressDTO();
        requestShippingAddressDTO.setShipName(customer.getContactName());
        requestShippingAddressDTO.setShipAddress(profile.getAddress());
        requestShippingAddressDTO.setShipCity(profile.getCity());
        requestShippingAddressDTO.setShipDistrict(profile.getDistrict());
        requestShippingAddressDTO.setShipWard(profile.getWard());
        requestShippingAddressDTO.setPhone(profile.getPhone());
        requestShippingAddressDTO.setCustomerId(customer.getCustomerId());
        requestShippingAddressDTO.setIsDefault(Boolean.TRUE);

        shippingAddressUpdateService.createShippingAddress(requestShippingAddressDTO);
    }
}
