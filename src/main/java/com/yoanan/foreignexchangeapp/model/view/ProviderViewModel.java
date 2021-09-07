package com.yoanan.foreignexchangeapp.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProviderViewModel {

    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;

    public ProviderViewModel() {
    }

    public ProviderViewModel(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public ProviderViewModel setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }
}
