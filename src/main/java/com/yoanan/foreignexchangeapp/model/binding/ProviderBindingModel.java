package com.yoanan.foreignexchangeapp.model.binding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderBindingModel {

    private String result;

    @JsonProperty("error-type")
    private String errorType;

    @JsonProperty("base_code")
    private String baseCode;

    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> conversionRates;

    private String documentation;

    @JsonProperty("terms-of-use")
    private String termsOfUse;

    @JsonProperty("time_last_update_unix")
    private Long lastUpdateUnix;

    @JsonProperty("time_next_update_unix")
    private Long nextUpdateUnix;

    public ProviderBindingModel() {
    }

    public ProviderBindingModel(String result, String errorType) {
        this.result = result;
        this.errorType = errorType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public Map<String, BigDecimal> getConversionRates() {
        return conversionRates != null ? conversionRates : Map.of();
    }

    public void setConversionRates(Map<String, BigDecimal> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public boolean isSuccess() {
        return "success".equals(result);
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public Long getLastUpdateUnix() {
        return lastUpdateUnix;
    }

    public void setLastUpdateUnix(Long lastUpdateUnix) {
        this.lastUpdateUnix = lastUpdateUnix;
    }

    public Long getNextUpdateUnix() {
        return nextUpdateUnix;
    }

    public void setNextUpdateUnix(Long nextUpdateUnix) {
        this.nextUpdateUnix = nextUpdateUnix;
    }

}
