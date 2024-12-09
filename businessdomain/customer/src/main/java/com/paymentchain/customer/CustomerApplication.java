package com.paymentchain.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Entry point for the Customer Service application.
 * This application leverages Spring Boot for rapid application development.
 */
@SpringBootApplication
public class CustomerApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command-line arguments (optional)
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    /**
     * Creates a {@link WebClient.Builder} bean with load-balancing capabilities.
     * The {@code @LoadBalanced} annotation ensures that requests are distributed
     * across available instances of a service registered with the discovery server.
     *
     * @return a configured {@link WebClient.Builder} instance
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalanceWebClientBuilder() {
        return WebClient.builder();
    }
}
