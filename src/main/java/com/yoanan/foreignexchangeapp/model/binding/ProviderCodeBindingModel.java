package com.yoanan.foreignexchangeapp.model.binding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProviderCodeBindingModel {

    private String result;
    @JsonProperty("supported_codes")
    private List<List<String>> supportedCodes;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<List<String>> getSupportedCodes() {
        return supportedCodes;
    }

    public void setSupportedCodes(List<List<String>> supportedCodes) {
        this.supportedCodes = supportedCodes;
    }

    public boolean isSuccess() {
        return "success".equals(result);
    }
}
