package com.microservices.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    // it will add load balancing capability to our webClient
    @LoadBalanced
   public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
