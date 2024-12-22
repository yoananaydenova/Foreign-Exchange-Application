package com.yoanan.foreignexchangeapp;

import com.yoanan.foreignexchangeapp.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class ForeignExchangeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeAppApplication.class, args);
    }

}
