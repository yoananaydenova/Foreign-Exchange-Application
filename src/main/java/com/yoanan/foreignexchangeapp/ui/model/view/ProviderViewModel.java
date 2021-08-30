package com.yoanan.foreignexchangeapp.ui.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProviderViewModel {

    @JsonProperty("exchange_rate")
    private Double exchangeRate;

    public ProviderViewModel() {
    }

    public ProviderViewModel(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public ProviderViewModel setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }
}
