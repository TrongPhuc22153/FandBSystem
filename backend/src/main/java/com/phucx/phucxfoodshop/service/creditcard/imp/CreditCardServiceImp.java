package com.phucx.phucxfoodshop.service.creditcard.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CreditCard;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.repository.CreditCardRepository;
import com.phucx.phucxfoodshop.service.creditcard.CreditCardService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreditCardServiceImp implements CreditCardService{
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserService userService;

    private final String SECRET_KEY = "969c4456-6a4b-46a5phucxfoodshop-9fd7-e1f408c0e6fa";


    @Override
    public Boolean updateCreditCard(CreditCard creditCard) {
        log.info("updateCreditCard(userID={})", creditCard.getUserID());
        return creditCardRepository.updateCreditCard(creditCard.getUserID(), 
            creditCard.getName(), creditCard.getNumber(), creditCard.getExpirationDate(), 
            creditCard.getSecurityCode(), SECRET_KEY);
    }


    @Override
    public CreditCard getCreditCard(String userID) throws NotFoundException {
        log.info("getCreditCard(userID={})", userID);
        
        CreditCard creditCard = creditCardRepository.getCreditCard(userID, SECRET_KEY)
            .orElseThrow(
                ()-> new NotFoundException("Credit card of user " + userID + " does not found!" )
            );

        if(creditCard.getNumber()!=null && creditCard.getSecurityCode()!=null){
            StringBuilder cardNumber = new StringBuilder(creditCard.getNumber());
            StringBuilder securityCode = new StringBuilder(creditCard.getSecurityCode());
            for(int i=0;i<cardNumber.length()-4;i++){
                cardNumber.setCharAt(i, '*');
            }
            for(int i=0;i<securityCode.length();i++){
                securityCode.setCharAt(i, '*');
            }
    
            creditCard.setSecurityCode(securityCode.toString());
            creditCard.setNumber(cardNumber.toString());
        }
        return creditCard;
    }


    @Override
    public CreditCard getCreditCardByUsername(String username)throws NotFoundException {
        log.info("getCreditCardByUsername(username={})", username);
        User user = this.userService.getUser(username);
        return this.getCreditCard(user.getUserID());
    }


    @Override
    public Boolean updateCreditCardByUsername(CreditCard creditCard, String username) {
        log.info("updateCreditCardByUsername(username={})", username);
        User user = userService.getUser(username);
        creditCard.setUserID(user.getUserID());
        return this.updateCreditCard(creditCard);
    }
    
}
