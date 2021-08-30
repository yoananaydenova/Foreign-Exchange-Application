package com.yoanan.foreignexchangeapp.ui.model.service;

import java.time.LocalDate;

public class ProviderServiceModel {


    private LocalDate date;
    private String baseCurrency;
    private String quoteCurrency;
    private Double exchangeRate;

    public ProviderServiceModel() {
    }

    public ProviderServiceModel( LocalDate date, String baseCurrency, String quoteCurrency, Double exchangeRate) {

        this.date = date;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.exchangeRate = exchangeRate;
    }



    public LocalDate getDate() {
        return date;
    }

    public ProviderServiceModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public ProviderServiceModel setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        return this;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public ProviderServiceModel setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
        return this;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public ProviderServiceModel setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }
}
