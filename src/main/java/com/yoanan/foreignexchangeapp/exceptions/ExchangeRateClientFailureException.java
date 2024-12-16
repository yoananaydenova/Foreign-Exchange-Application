package com.yoanan.foreignexchangeapp.exceptions;

public class ExchangeRateClientFailureException extends RuntimeException {
    public ExchangeRateClientFailureException(String message) {
        super(message);
    }
    public ExchangeRateClientFailureException(){
        this("Exchange rate service failed");
    }
}
