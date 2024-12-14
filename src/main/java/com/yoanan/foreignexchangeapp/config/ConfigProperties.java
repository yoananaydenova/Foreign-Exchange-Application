package com.yoanan.foreignexchangeapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("foreign.exchange.app")
public record ConfigProperties(String apiUrl, String apiVersion) {
}
