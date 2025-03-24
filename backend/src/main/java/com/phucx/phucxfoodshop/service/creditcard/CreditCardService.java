package com.phucx.phucxfoodshop.service.creditcard;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CreditCard;

public interface CreditCardService {
    public Boolean updateCreditCard(CreditCard creditCard);
    public Boolean updateCreditCardByUsername(CreditCard creditCard, String username);

    public CreditCard getCreditCard(String userID) throws NotFoundException;
    public CreditCard getCreditCardByUsername(String username) throws NotFoundException;
}
