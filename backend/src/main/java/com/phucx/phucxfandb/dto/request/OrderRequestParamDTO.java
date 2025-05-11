package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.constant.WebConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestParamDTO {
    private String field = "orderDate";
    private Sort.Direction direction = Sort.Direction.DESC;
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
    private OrderType type;
    private OrderStatus status;
}
