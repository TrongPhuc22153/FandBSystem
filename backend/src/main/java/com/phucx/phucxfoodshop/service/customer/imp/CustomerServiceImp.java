package com.phucx.phucxfoodshop.service.customer.imp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidUserException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.CustomerDetails;
import com.phucx.phucxfoodshop.model.CustomerDetailsBuilder;
import com.phucx.phucxfoodshop.model.CustomerFullDetails;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserDetails;
import com.phucx.phucxfoodshop.model.UserVerification;
import com.phucx.phucxfoodshop.model.VerificationInfo;
import com.phucx.phucxfoodshop.repository.CustomerDetailRepository;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.email.EmailService;
import com.phucx.phucxfoodshop.service.image.CustomerImageService;
import com.phucx.phucxfoodshop.service.user.UserProfileService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private CustomerDetailRepository customerDetailRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerImageService customerImageService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private EmailService emailService;

    private final String CUSTOMER = "CUSTOMER";

	@Override
	public CustomerDetail updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException {
        log.info("updateCustomerInfo({})", customer.toString());
        CustomerDetail fetchedCustomer = customerDetailRepository.findById(customer.getCustomerID())
            .orElseThrow(
                ()->new CustomerNotFoundException("Customer " + customer.getCustomerID() + " does not found"))
            ;

        String picture = ImageUtils.getImageName(customer.getPicture());
        Boolean result = customerDetailRepository.updateCustomerInfo(
            fetchedCustomer.getCustomerID(), customer.getContactName(), 
            customer.getAddress(), customer.getCity(), customer.getDistrict(), 
            customer.getWard(), customer.getPhone(), picture);
        if(!result) throw new RuntimeException("Error when update information of customer " + customer.getCustomerID());
        
        customer.setPicture(picture);
        customerImageService.setCustomerDetailImage(customer);
        return customer;
	}
	@Override
	public CustomerDetail getCustomerDetail(String userID) throws CustomerNotFoundException{
        log.info("getCustomerDetail(userID={})", userID);
        CustomerDetail customer =customerDetailRepository.findByUserID(userID).orElseThrow(
            ()-> new CustomerNotFoundException("Customer with userId: " + userID + " does not found"));
        return customer;
    }
    
    @Override
    public CustomerDetail addNewCustomer(CustomerDetail customer) throws InvalidUserException{
        log.info("addNewCustomer({})", customer);
        if(customer.getContactName()==null) throw new InvalidUserException("Missing contact name");
        if(customer.getUserID()==null) throw new InvalidUserException("Missing userId");

        // check customer
        Optional<CustomerDetail> fetchedCustomer = customerDetailRepository.findByUserID(customer.getUserID());
        if(fetchedCustomer.isPresent()) 
            throw new EntityExistsException("Customer with userId " + customer.getUserID() + " is existed");
        // add new customer 
        String profileID = UUID.randomUUID().toString();
        String customerID = UUID.randomUUID().toString();

        Boolean status = customerDetailRepository.addNewCustomer(
            profileID, customer.getUserID(), customerID, customer.getContactName());
        if(!status) throw new RuntimeException("Customer can not be created!");
        return new CustomerDetail(customerID, customer.getUserID(), customer.getContactName());
    }
	
	@Override
	public CustomerDetail getCustomerByID(String customerID) throws CustomerNotFoundException {
        log.info("getCustomerByID(customerID={})", customerID);
		CustomerDetail customer = customerDetailRepository.findById(customerID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer " + customerID + " does not found"));
        customerImageService.setCustomerDetailImage(customer);
        return customer;
	}
    

    @Override
    public CustomerDetail getCustomerByUserID(String userID) throws CustomerNotFoundException {
        log.info("getCustomerByUserID(userID={})", userID);
        CustomerDetail customer = customerDetailRepository.findByUserID(userID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer with userID " + userID + " does not found"));
        customerImageService.setCustomerDetailImage(customer);
        return customer;
    }

    @Override
    public List<CustomerDetail> getCustomersByIDs(List<String> customerIDs) {
        log.info("getCustomersByIDs(customerIDs={})", customerIDs);
        List<CustomerDetail> customers = customerDetailRepository.findAllById(customerIDs);
        customerImageService.setCustomerDetailImage(customers);
        return customers;
    }
    
    @Override
    public CustomerFullDetails getCustomerDetails(String userID) {
        log.info("getCustomerDetails(userID={})", userID);
        // get customer details
        CustomerDetail customerDetail = this.getCustomerDetail(userID);
        // fetch user
        User fetchedUser = userService.getUserById(userID);
        // get user verification
        UserVerification userVerification = userProfileService.getUserVerification(userID);
        VerificationInfo verificationInfo = new VerificationInfo(
            fetchedUser.getEmailVerified(),
            userVerification.getPhoneVerification(), 
            userVerification.getProfileVerification());

        String picture = customerDetail.getPicture()!=null?customerImageService.getImageUrl(customerDetail.getPicture()):null;
        // assemble customer details
        CustomerDetails customerDetails = new CustomerDetailsBuilder()
            .withCustomerID(customerDetail.getCustomerID())
            .withUserID(userID)
            .withContactName(customerDetail.getContactName())
            .withAddress(customerDetail.getAddress())
            .withWard(customerDetail.getWard())
            .withDistrict(customerDetail.getDistrict())
            .withCity(customerDetail.getCity())
            .withPhone(customerDetail.getPhone())
            .withPicture(picture)
            .withUsername(fetchedUser.getUsername())
            .withFirstName(fetchedUser.getFirstName())
            .withLastName(fetchedUser.getLastName())
            .withEmail(fetchedUser.getEmail())
            .build();
        
        return new CustomerFullDetails(customerDetails, verificationInfo);
    }
    @Override
    public List<CustomerDetail> getCustomersByUserIDs(List<String> userIDs) {
        log.info("getCustomersByUserIDs(userIDs={})", userIDs);
        List<CustomerDetail> customerDetails = customerDetailRepository.findAllByUserID(userIDs);

        customerImageService.setCustomerDetailImage(customerDetails);

        return customerDetails;
    }
    @Override
    public CustomerDetail getCustomerByUsername(String username) throws CustomerNotFoundException {
        log.info("getCustomerByUsername(username={})", username);
        User user = userService.getUser(username);
        return this.getCustomerByUserID(user.getUserID());
    }
    @Override
    public CustomerFullDetails getCustomerDetailsByUsername(String username){
        log.info("getCustomerDetailsByUsername(username={})", username);
        User user = userService.getUser(username);
        return this.getCustomerDetails(user.getUserID());
    }
    @Override
    public Page<UserDetails> getUsers(Integer pagenumber) {
        log.info("getUsers(pagenumber={})", pagenumber);
        Page<UserDetails> users = userService.getUsersByRole(CUSTOMER, pagenumber);
        users.getContent().stream().forEach(customer -> {
            if(customer.getPicture()!=null){
                String imageUrl = customerImageService.getImageUrl(customer.getPicture());
                customer.setPicture(imageUrl);
            }
        });
        return users;
    }
    @Override
    public void sendVerificationEmail(String username, String baseUrl) {
        log.info("sendVerificationEmail(username={})", username);
        User user = userService.getUser(username);
        emailService.sendVerificationEmail(user.getEmail(), username, baseUrl);
    }
}
