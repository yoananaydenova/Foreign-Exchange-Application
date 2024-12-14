package com.yoanan.foreignexchangeapp.controller;

import com.yoanan.foreignexchangeapp.config.ConfigProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/about")
public class AboutController {

    private final ConfigProperties configProperties;

    public AboutController(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @GetMapping
    public Map<String,String> showProps() {
        return Map.of("apiUrl", configProperties.apiUrl(),
                "apiVersion", configProperties.apiVersion());
    }
}
