package com.phucx.phucxfoodshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFullDetails {
    private CustomerDetails customerDetails;
    private VerificationInfo verificationInfo;
}
