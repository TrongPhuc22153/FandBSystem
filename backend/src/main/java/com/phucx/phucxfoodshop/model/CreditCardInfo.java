package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardInfo {
    private String name;
    private String number;
    private String expirationDate;
    private String securityCode;
    private String userID;
}
