/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.paymentchain.setups;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author rvega
 */

/**
 * Configuration class for setting up WebClient with load balancing capabilities.
 * This ensures that WebClient can resolve service names via Eureka or other discovery clients.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient.Builder bean configured with load balancing support.
     * The @LoadBalanced annotation allows the WebClient to interact with service instances
     * registered in a discovery server like Eureka.
     *
     * @return a load-balanced WebClient.Builder instance
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}

