/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.setups;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

/**
 *
 * @author rvega
 */

/**
 * Gateway filter for handling authentication and role validation.
 * This filter checks the presence and structure of the Authorization header,
 * verifies the bearer token, and ensures the user has the required roles.
 */
@Slf4j
@Component
public class AuthenticationFiltering extends AbstractGatewayFilterFactory<AuthenticationFiltering.Config> {

    private final WebClient.Builder webClientBuilder;

    /**
     * Constructor for injecting the WebClient.Builder dependency.
     *
     * @param webClientBuilder the WebClient.Builder used for making HTTP requests.
     */
    public AuthenticationFiltering(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Applies the filter logic to validate the Authorization header and user roles.
     *
     * @param config the configuration object for this filter (currently unused).
     * @return a GatewayFilter instance.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            // Check for Authorization header in the incoming request
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header");
            }

            // Extract and validate the Authorization header structure
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");
            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad Authorization structure");
            }

            // Verify token and roles using WebClient
            return webClientBuilder.build()
                    .get()
                    .uri("http://keycloack/roles") // URI to validate roles
                    .header(HttpHeaders.AUTHORIZATION, parts[1])
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(response -> {
                        if (response != null) {
                            log.info("See Objects: " + response);
                            // Check for "Partners" role in the response
                            if (response.get("Partners") == null || StringUtils.isEmpty(response.get("Partners").asText())) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Role Partners missing");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Roles missing");
                        }
                        return exchange;
                    })
                    .onErrorMap(error -> {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Communication Error", error.getCause());
                    })
                    .flatMap(chain::filter);
        }, 1);
    }

    /**
     * Configuration class for the AuthenticationFiltering filter.
     * Currently, it does not contain any configurable properties but serves as a placeholder for future enhancements.
     */
    public static class Config {
    }
}
