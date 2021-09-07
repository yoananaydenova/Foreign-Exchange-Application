package com.yoanan.foreignexchangeapp.service;


import java.math.BigDecimal;

public interface ExchangeRateClientService {

    BigDecimal getExchangeRate(String base, String quote);
}
