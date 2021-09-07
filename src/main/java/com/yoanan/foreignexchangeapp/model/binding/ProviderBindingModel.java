package com.yoanan.foreignexchangeapp.model.binding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
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
    private Map<String, BigDecimal> rates = new HashMap<>();

    private Map<String, String> error = new HashMap<>();

    public ProviderBindingModel() {
    }

    public ProviderBindingModel(boolean state, Timestamp timestamp, String baseCurrency, LocalDate date, Map<String, BigDecimal> rates, Map<String, String> error) {
        this.state = state;
        this.timestamp = timestamp;
        this.baseCurrency = baseCurrency;
        this.date = date;
        this.rates = rates;
        this.error = error;
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

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public ProviderBindingModel setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
        return this;
    }

    public Map<String, String> getError() {
        return error;
    }

    public ProviderBindingModel setError(Map<String, String> error) {
        this.error = error;
        return this;
    }
}
