package com.phucx.phucxfoodshop.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemKey {
    private String reservationID;
    private Integer productID;
}
