package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderMenuItemDTO {
    private Long orderMenuItemId;

    private Order order;

    private RequestMenuItemDTO menuItem;

    private Integer quantity;
}
