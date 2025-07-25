package com.yoanan.foreignexchangeapp.model.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class TransactionBindingModel {


    @JsonProperty("source_amount")
    private BigDecimal sourceAmount;
    @JsonProperty("source_currency")
    private String sourceCurrency;
    @JsonProperty("target_currency")
    private String targetCurrency;

    public TransactionBindingModel() {
    }

    public TransactionBindingModel(BigDecimal sourceAmount, String sourceCurrency, String targetCurrency) {
        this.sourceAmount = sourceAmount;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    @NotNull(message = "Source amount must be present!")
    @DecimalMin(value = "0", message = "Source amount must be greater than 0!")
    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public TransactionBindingModel setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    @NotNull(message = "Source currency must be present!")
    @Length(min = 3, max = 3, message = "The length of the source currency must be 3 symbols!")
    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public TransactionBindingModel setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    @NotNull(message = "Target currency must be present!")
    @Length(min = 3, max = 3, message = "The length of the source currency must be 3 symbols!")
    public String getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionBindingModel setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }
}
