package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.entity.CartItem;
import com.phucx.phucxfandb.entity.MenuItem;
import com.phucx.phucxfandb.entity.OrderDetail;
import com.phucx.phucxfandb.enums.MenuItemStatus;
import com.phucx.phucxfandb.enums.OrderItemStatus;

import java.math.BigDecimal;
import java.util.List;

public class PriceUtils {
    public static BigDecimal calculateReservationTotalPrice(List<MenuItem> menuItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (MenuItem item : menuItems) {
            if(!MenuItemStatus.CANCELED.equals(item.getStatus())){
                totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return totalPrice;
    }

    public static BigDecimal calculateOrderTotalPrice(List<OrderDetail> orderDetails) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails) {
            if(!OrderItemStatus.CANCELED.equals(orderDetail.getStatus())){
                totalPrice = totalPrice.add(orderDetail.getUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            }
        }
        return totalPrice;
    }

    public static BigDecimal calculateTotalPrice(List<CartItem> cartItems){
        return cartItems.stream()
                .map(item -> item
                        .getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
