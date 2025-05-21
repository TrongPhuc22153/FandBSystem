package com.phucx.phucxfandb.service.paypal;

import com.phucx.phucxfandb.dto.response.PayPalResponseDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;

import java.io.IOException;

public interface PayPalService {
    PayPalResponseDTO createOrder(double amount, String currency, String returnUrl, String cancelUrl) throws IOException;
    ResponseDTO<Void> captureOrder(String orderId) throws IOException;
}
