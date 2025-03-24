package com.phucx.phucxfoodshop.service.currency;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.phucx.phucxfoodshop.constant.Currency;
import com.phucx.phucxfoodshop.constant.PaymentConstant;
import com.phucx.phucxfoodshop.utils.BigDecimalUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrencyServiceImp implements CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImp(){
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Double getExchangeRate(Currency currency) {
        log.info("getExchangeRate()");
        ResponseEntity<Map> response = restTemplate. getForEntity(PaymentConstant.EXCHANGE_RATE_URL, Map.class);
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        return (Double) rates.get(currency.name());
    }

    @Override
    public String exchangeRateFromUSDToVND(Double amount) {
        log.info("exchangeRateFromUSD(amount={})", amount);
        Double rate = this.getExchangeRate(Currency.VND);
        Double value = rate*amount;
        return String.valueOf(Math.round(value));
    }

    @Override
    public String exchangeRateFromVNDToUSD(Long amount) {
        log.info("exchangeRateFromVNDToUSD(amount={})", amount);
        Double rate = this.getExchangeRate(Currency.VND);
        Double value = amount.doubleValue()/rate;
        return BigDecimalUtils.formatter(new BigDecimal(value)).toString();
    }
    
}
