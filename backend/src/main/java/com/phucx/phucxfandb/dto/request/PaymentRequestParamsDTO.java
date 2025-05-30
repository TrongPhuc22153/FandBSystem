package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestParamsDTO extends PaginationParamsDTO {
    private String field = "createdAt";
    private PaymentStatus status;
    private String orderId;
    private String phone;
    private String contactName;
    private Integer tableNumber;
    private String search;
}
