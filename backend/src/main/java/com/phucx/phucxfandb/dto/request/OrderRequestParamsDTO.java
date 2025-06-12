package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestParamsDTO extends PaginationParamsDTO {
    private String field = "orderDate";
    private Sort.Direction direction = Sort.Direction.DESC;
    private OrderType type;
    private List<OrderStatus> status;
    private String search;
    private LocalDate startDate;
    private LocalDate endDate;
}
