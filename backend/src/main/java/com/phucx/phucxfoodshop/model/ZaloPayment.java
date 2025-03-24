package com.phucx.phucxfoodshop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class ZaloPayment extends PaymentDTO {
    private String appTransId;
}
