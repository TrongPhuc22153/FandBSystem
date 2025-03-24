package com.phucx.phucxfoodshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String>{
    @Procedure("UpdateCreditCard")
    public Boolean updateCreditCard(String userId, 
        String creditName, String creditNumber, String expirationDate, 
        String securityCode, String privateKey);

    @Query(nativeQuery = true, value =  """
        CALL GetCreditCard(?1, ?2)
            """)
    public Optional<CreditCard> getCreditCard(String userId, String privateKey);
}
