package com.phucx.phucxfandb.aspect;

import com.phucx.phucxfandb.entity.Cart;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.repository.CartRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LazyCartInitializationAspect {
    private final CartRepository cartRepository;
    private final CustomerReaderService customerReaderService;

    @Transactional
    @Before("@annotation(com.phucx.phucxfandb.annotation.EnsureCartExists) && args(username, ..)")
    public void ensureCartExists(JoinPoint joinPoint, String username){
        log.info("ensureCartExists(executing={}, username={})", joinPoint.getSignature(), username);
        if(!cartRepository.existsByCustomerProfileUserUsername(username)){
            Customer customer = customerReaderService.getCustomerEntityByUsername(username);
            Cart newCart = new Cart();
            newCart.setCustomer(customer);
            cartRepository.save(newCart);
        }
    }



}
