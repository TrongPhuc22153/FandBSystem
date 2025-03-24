package com.phucx.phucxfoodshop.service.currency;

import com.phucx.phucxfoodshop.constant.Currency;

public interface CurrencyService {
    public Double getExchangeRate(Currency currency);
    public String exchangeRateFromUSDToVND(Double amount);
    public String exchangeRateFromVNDToUSD(Long amount);
}
