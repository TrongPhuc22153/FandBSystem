package com.phucx.phucxfoodshop.service.paymentHandler.imp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.phucxfoodshop.constant.PaymentConstant;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.PaymentDTO;
import com.phucx.phucxfoodshop.service.currency.CurrencyService;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.paymentHandler.MomoHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonoHandlerServiceImp implements MomoHandlerService{
    // @Value("${momo.paygate}")
    private String momoPayGate = "https://test-payment.momo.vn";
    @Value("${momo.partner-code}")
    private String partnerCode;
    @Value("${momo.access-key}")
    private String accessKey;
    @Value("${momo.secret-key}")
    private String secretKey;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrencyService currencyService;
    private RestTemplate restTemplate;
    private String endpoint = momoPayGate + "/v2/gateway/api/create";
    @Autowired
    private PaymentManagementService paymentManagementService;

    public MonoHandlerServiceImp() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String createPayment(PaymentDTO paymentDTO) {
        log.info("createPayment(payment={})", paymentDTO);
        try {
            String extraData = "";
            String orderId = paymentDTO.getOrderID();
            String orderInfo = "Thanh toan qua MoMo";
            String requestType = "payWithATM";
            String requestId = String.valueOf(System.currentTimeMillis());
            String redirectUrl = paymentDTO.getBaseUrl() + PaymentConstant.MOMO_SUCCESSFUL_URL;
            String ipnUrl = paymentDTO.getBaseUrl() + PaymentConstant.MOMO_SUCCESSFUL_URL;
            String value = currencyService.exchangeRateFromUSDToVND(paymentDTO.getAmount());
            String amount = String.valueOf(value);
            String rawHash = "accessKey=" + accessKey + "&amount=" + amount + "&extraData=" + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl + "&requestId=" + requestId + "&requestType=" + requestType;
            // String signature = this.Sha256(rawHash);
            // Generate the HMAC SHA256 signature
            String signature = this.hmacSha256(rawHash, secretKey);
            Map<String, String> params = new HashMap<>();
            params.put("partnerCode", partnerCode);
            params.put("partnerName", "Test");
            params.put("storeId", "MomoTestStore");
            params.put("requestId", requestId);
            params.put("amount", amount);
            params.put("orderId", orderId);
            params.put("orderInfo", orderInfo);
            params.put("redirectUrl", redirectUrl);
            params.put("ipnUrl", ipnUrl);
            params.put("lang", "vi");
            params.put("extraData", extraData);
            params.put("requestType", requestType);
            params.put("signature", signature);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String json = objectMapper.writeValueAsString(params);
            HttpEntity<String> entity  = new HttpEntity<>(json, headers);

            Map<String, String> response = restTemplate.postForObject(endpoint, entity, Map.class);
            if(response==null) return null;
            log.info("Momo response: {}", response);
            // save payment
            this.savePayment(paymentDTO);
            return response.get("payUrl");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    // save payment to database
    private void savePayment(PaymentDTO paymentDTO){
        log.info("savePayment(payment={})", paymentDTO);
        LocalDateTime createdTime = LocalDateTime.now();
        // payment method
        String method = paymentDTO.getMethod();
        String state = PaymentStatusConstant.CREATED.name().toLowerCase();
        String paymentID = UUID.randomUUID().toString();
        // save payment
        Boolean result = paymentManagementService.savePayment(
            paymentID, createdTime, paymentDTO.getAmount(), state, 
            paymentDTO.getCustomerID(), paymentDTO.getOrderID(), method);
        if(!result){
            log.error("Error while saving payment: {}", paymentDTO);
        }
    }

    // hash 
    private String hmacSha256(String message, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(message.getBytes("UTF-8"));
        return bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public Boolean paymentSuccessfully(String orderID){
        log.info("paymentSuccessfully(orderID={})", orderID);
        try {
            return paymentManagementService.updatePaymentStatusByOrderID(orderID, PaymentStatusConstant.SUCCESSFUL);
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }

    }
    
}
