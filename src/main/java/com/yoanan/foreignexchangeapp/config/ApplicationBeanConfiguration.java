package com.yoanan.foreignexchangeapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationBeanConfiguration {

    private static final String BASE_URL = "http://data.fixer.io/api";

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        TypeMap<TransactionBindingModel, TransactionServiceModel> typeMap = modelMapper.createTypeMap(TransactionBindingModel.class, TransactionServiceModel.class);
//        System.out.println();
//        typeMap.addMappings(m -> m.map(TransactionBindingModel::getRates,
//                TransactionServiceModel::setQuoteCurrency));
//
//        typeMap.addMappings(m -> m.map(TransactionBindingModel::getRates,
//                TransactionServiceModel::setExchangeRate));

        return modelMapper;
    }


    @Bean
    public WebClient localApiClient() {
        return WebClient.create(BASE_URL);
    }


}
