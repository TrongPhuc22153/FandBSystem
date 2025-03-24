package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.compositeKey.MenuItemKey;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MenuItemKey.class)
@Table(name = "menuitems")
public class MenuItem {
    @Id
    private String reservationID;
    @Id
    private Integer productID;

    private BigDecimal unitPrice;
    private Integer quantity;
}
