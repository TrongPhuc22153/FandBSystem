package com.phucx.phucxfoodshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdminDetails extends CustomerDetails {
    private Boolean phoneVerified;
    private Boolean profileVerified;
    private Boolean emailVerified;
    private Boolean enabled;

}
