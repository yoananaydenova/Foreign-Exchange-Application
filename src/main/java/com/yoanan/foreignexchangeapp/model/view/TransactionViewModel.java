package com.yoanan.foreignexchangeapp.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionViewModel {

    @JsonView(Views.TransactionView.class)
    private String id;

   // private LocalDate date;
    private String date;

    @JsonProperty("source_currency")
    private String sourceCurrency;


    @JsonProperty("target_currency")
    private String targetCurrency;


    @JsonProperty("exchange_rate")
    private Double exchangeRate;

    @JsonProperty("source_amount")
    private BigDecimal sourceAmount;

    @JsonView(Views.TransactionView.class)
    @JsonProperty("target_amount")
    private BigDecimal targetAmount;

    public TransactionViewModel() {
    }




    public TransactionViewModel(String id, String date, String sourceCurrency, String targetCurrency, Double exchangeRate, BigDecimal sourceAmount, BigDecimal targetAmount) {
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

    public TransactionViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getDate() {
        return date;
    }

    public TransactionViewModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public TransactionViewModel setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionViewModel setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public TransactionViewModel setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public TransactionViewModel setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public TransactionViewModel setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }
}
