package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class config {

//    @Bean
    public RestClient restTemplate() {
        return RestClient.create();
    }
}
