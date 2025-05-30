package com.phucx.phucxfandb.service.paymentMethod;


import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodReaderService {
    List<PaymentMethodDTO> getPaymentMethods(String type);
    PaymentMethodDTO getPaymentMethod(String methodId);
    PaymentMethod getPaymentMethodEntityByName(String methodName);
}
