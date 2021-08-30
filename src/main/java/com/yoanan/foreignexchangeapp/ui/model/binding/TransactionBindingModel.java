package com.yoanan.foreignexchangeapp.ui.model.binding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionBindingModel {

    private String id;
    private LocalDate date;
    @JsonProperty("source_amount")
    private BigDecimal sourceAmount;
    @JsonProperty("source_currency")
    private String sourceCurrency;
    @JsonProperty("target_currency")
    private String targetCurrency;

    public TransactionBindingModel() {
    }

    public String getId() {
        return id;
    }

    public TransactionBindingModel setId(String id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionBindingModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public TransactionBindingModel setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public TransactionBindingModel setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionBindingModel setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }
}
