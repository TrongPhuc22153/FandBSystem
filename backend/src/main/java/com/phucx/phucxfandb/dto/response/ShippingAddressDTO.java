package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShippingAddressDTO {
    Long id;
    String shipName;
    String shipAddress;
    String shipCity;
    String shipDistrict;
    String shipWard;
    String phone;
    Boolean isDefault;
}
