package com.yoanan.foreignexchangeapp.model.binding;

import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TransactionListBindingModel {

    private String id;
    private LocalDate date;

    public TransactionListBindingModel() {
    }

    public TransactionListBindingModel(String id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    @Length(min = 36, max = 36, message = "The length of the transaction ID must be 36 symbols!")
    public String getId() {
        return id;
    }

    public TransactionListBindingModel setId(String id) {
        this.id = id;
        return this;
    }

    @PastOrPresent(message = "The date must be in past or present!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    public TransactionListBindingModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }
}

