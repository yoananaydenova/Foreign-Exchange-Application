package com.yoanan.foreignexchangeapp.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transactions")
public class TransactionEntity {


    private String id;
    private LocalDate date;
    private String sourceCurrency;
    private String targetCurrency;
    private Double exchangeRate;
    private BigDecimal sourceAmount;
    private BigDecimal targetAmount;

    public TransactionEntity() {
    }

    public TransactionEntity(String id, LocalDate date, String sourceCurrency, String targetCurrency, Double exchangeRate, BigDecimal sourceAmount, BigDecimal targetAmount) {
        this.id = id;
        this.date = date;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.sourceAmount = sourceAmount;
        this.targetAmount = targetAmount;
    }

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(name="uuid-string",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    public TransactionEntity setId(String id) {
        this.id = id;
        return this;
    }

    @Column(name="date", columnDefinition = "DATE",nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public TransactionEntity setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @Column(name="source_currency", nullable = false)
    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public TransactionEntity setSourceCurrency(String baseCurrency) {
        this.sourceCurrency = baseCurrency;
        return this;
    }

    @Column(name="target_currency", nullable = false)
    public String getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionEntity setTargetCurrency(String quoteCurrency) {
        this.targetCurrency = quoteCurrency;
        return this;
    }

    @Column(name="exchange_rate", nullable = false)
    public Double getExchangeRate() {
        return exchangeRate;
    }

    public TransactionEntity setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    @Column(name="source_amount", nullable = false)
    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public TransactionEntity setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    @Column(name="target_amount", nullable = false)
    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public TransactionEntity setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }
}
