package com.phucx.phucxfandb.service.paymentMethod;


import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentMethodReaderService {
    List<PaymentMethodDTO> getPaymentMethods();
    PaymentMethod getPaymentMethodEntity(String id);
    Page<PaymentMethodDTO> getPaymentMethods(int pageNumber, int pageSize);
    PaymentMethodDTO getPaymentMethod(String methodId);
}
