package com.yoanan.foreignexchangeapp.ui.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionServiceModel {

    private String id;
    private LocalDate date;
    private String sourceCurrency;
    private String targetCurrency;
    private Double exchangeRate;
    private BigDecimal sourceAmount;
    private BigDecimal targetAmount;

    public TransactionServiceModel() {
    }

    public TransactionServiceModel(String id, LocalDate date, String sourceCurrency, String targetCurrency, Double exchangeRate, BigDecimal sourceAmount, BigDecimal targetAmount) {
        this.id = id;
        this.date = date;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.sourceAmount = sourceAmount;
        this.targetAmount = targetAmount;
    }

    public String getId() {
        return id;
    }

    public TransactionServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionServiceModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public TransactionServiceModel setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionServiceModel setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public TransactionServiceModel setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public TransactionServiceModel setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public TransactionServiceModel setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }
}
