/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.paymentchain.setups;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author rvega
 */

// Annotation to enable Lombok's logging feature (Slf4j provides a logger instance named 'log')
@Slf4j
@Component
public class GlobalPreFiltering implements GlobalFilter {

    // Override the filter method to define a custom pre-filter logic
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log an informational message indicating that the global pre-filter was executed
        log.info("Global prefilter executed");
        // Continue the filter chain processing the next filter in the chain
        return chain.filter(exchange);
    }
}

