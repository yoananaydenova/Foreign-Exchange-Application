package com.yoanan.foreignexchangeapp.ui.model.binding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProviderBindingModel {

    @JsonProperty("success")
    private boolean state;
    private Timestamp timestamp;
    @JsonProperty("base")
    private String baseCurrency;
    private LocalDate date;
    private Map<String, Double> rates = new HashMap<>();

    public ProviderBindingModel() {
    }

    public ProviderBindingModel(boolean state, Timestamp timestamp, String baseCurrency, LocalDate date, Map<String, Double> rates) {
        this.state = state;
        this.timestamp = timestamp;
        this.baseCurrency = baseCurrency;
        this.date = date;
        this.rates = rates;
    }

    public boolean isState() {
        return state;
    }

    public ProviderBindingModel setState(boolean state) {
        this.state = state;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public ProviderBindingModel setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public ProviderBindingModel setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ProviderBindingModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public ProviderBindingModel setRates(Map<String, Double> rates) {
        this.rates = rates;
        return this;
    }
}
