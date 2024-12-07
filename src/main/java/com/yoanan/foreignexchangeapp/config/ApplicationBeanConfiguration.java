package com.yoanan.foreignexchangeapp.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationBeanConfiguration {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public WebClient localApiClient() {
        return WebClient.create(BASE_URL);
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
