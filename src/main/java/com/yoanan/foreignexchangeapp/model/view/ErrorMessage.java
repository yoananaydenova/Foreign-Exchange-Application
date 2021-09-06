package com.yoanan.foreignexchangeapp.model.view;

public class ErrorMessage {

    private String error;

    public ErrorMessage(String error) {
        setError(error);
    }

    public String getError() {
        return error;
    }

    public ErrorMessage setError(String error) {
        int index = error.indexOf(":") + 1;
        this.error = error.substring(index);
        return this;
    }
}
