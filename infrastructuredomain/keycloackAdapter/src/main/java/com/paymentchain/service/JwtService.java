/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.service;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
/**
 *
 * @author rvega
 */

/**
 * Service class for handling JWT operations.
 * This service provides methods to retrieve JSON Web Keys (JWK) for token verification.
 */
@Service
public class JwtService {

    /**
     * The URL to fetch the JSON Web Key Set (JWKS) from.
     * Populated from the 'keycloak.jwk-set-uri' property.
     */
    @Value("${keycloak.jwk-set-uri}")
    private String jwksUrl;

    /**
     * The ID of the specific certificate to retrieve from the JWKS.
     * Populated from the 'keycloak.certs-id' property.
     */
    @Value("${keycloak.certs-id}")
    private String certsId;

    /**
     * Retrieves the JWK (JSON Web Key) for the specified certificate ID.
     * The result is cached to improve performance and reduce redundant network calls.
     *
     * @return the JWK for the specified certificate ID.
     * @throws Exception if the JWK cannot be retrieved.
     */
    @Cacheable(value = "jwkCache")
    public Jwk getJwk() throws Exception {
        // Create a URL object for the JWKS endpoint.
        URL url = new URL(jwksUrl);

        // Use UrlJwkProvider to fetch the JWK from the specified URL.
        UrlJwkProvider urlJwkProvider = new UrlJwkProvider(url);

        // Retrieve the JWK corresponding to the trimmed certificate ID.
        Jwk jwk = urlJwkProvider.get(certsId.trim());

        return jwk;
    }
}

