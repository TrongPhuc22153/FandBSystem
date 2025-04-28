package com.phucx.phucxfandb.service.paymentMethod;

import com.phucx.phucxfandb.dto.request.RequestPaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodUpdateService {
    // update
    PaymentMethodDTO updatePaymentMethod(String methodId, RequestPaymentMethodDTO requestPaymentMethodDTO);
    // create
    PaymentMethodDTO createPaymentMethod(RequestPaymentMethodDTO requestPaymentMethodDTO);
    List<PaymentMethodDTO> createPaymentMethods(List<RequestPaymentMethodDTO> createPaymentMethodDTOs);
}
