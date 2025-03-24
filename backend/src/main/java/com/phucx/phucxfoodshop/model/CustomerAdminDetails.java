package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAdminDetails extends CustomerDetails {
    private Boolean phoneVerified;
    private Boolean profileVerified;
    private Boolean emailVerified;
    private Boolean enabled;
}
