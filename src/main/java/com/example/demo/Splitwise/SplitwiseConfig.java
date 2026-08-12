package com.example.demo.Splitwise;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class SplitwiseConfig {

    @Value("${splitwise.api.key}")
    private String apiKey;

    @Bean
    public RestClient splitwiseRestClient() {
        return RestClient.builder()
                .baseUrl("https://secure.splitwise.com/api/v3.0")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
    }