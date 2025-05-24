package com.phucx.phucxfandb.service.paymentMethod;


import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentMethodReaderService {
    List<PaymentMethodDTO> getPaymentMethods();
    PaymentMethod getPaymentMethodEntity(String id);
    PaymentMethodDTO getPaymentMethod(String methodId);
    PaymentMethod getPaymentMethodEntityByName(String methodName);
}
