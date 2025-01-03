/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.controller;

import com.paymentchain.service.KeycloakRestService;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.paymentchain.exception.BussinesRuleException;
import com.paymentchain.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author rvega
 */


/**
 * REST controller handling authentication and authorization operations.
 */
@RestController
public class IndexController {

    // Logger instance for recording events and errors
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    // Service for interacting with Keycloak
    @Autowired
    private KeycloakRestService restService;

    // Service for validating and decoding JWT tokens
    @Autowired
    private JwtService jwtService;

    /**
     * Retrieves roles from the provided JWT.
     * Validates the JWT, checks expiration, and extracts roles.
     *
     * @param authHeader the Authorization header containing the JWT token.
     * @return ResponseEntity with a map of roles and their lengths if valid.
     * @throws BussinesRuleException if the JWT is invalid or expired.
     */
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authHeader) throws BussinesRuleException {
        try {
            // Decode the JWT token
            DecodedJWT jwt = JWT.decode(authHeader.replace("Bearer", "").trim());

            // Validate the JWT signature using the public key
            Jwk jwk = jwtService.getJwk();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            // Extract roles from the JWT
            List<String> roles = (List<String>) jwt.getClaim("realm_access").asMap().get("roles");

            // Check if the token is expired
            Date expiryDate = jwt.getExpiresAt();
            if (expiryDate.before(new Date())) {
                throw new Exception("Token is expired");
            }

            // Map roles to their lengths
            HashMap<String, Integer> roleLengths = new HashMap<>();
            for (String role : roles) {
                roleLengths.put(role, role.length());
            }
            return ResponseEntity.ok(roleLengths);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
            throw new BussinesRuleException("01", e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Validates the provided JWT token.
     *
     * @param authHeader the Authorization header containing the JWT token.
     * @return ResponseEntity indicating whether the token is valid.
     * @throws BussinesRuleException if the token is invalid.
     */
    @GetMapping("/valid")
    public ResponseEntity<?> valid(@RequestHeader("Authorization") String authHeader) throws BussinesRuleException {
        try {
            restService.checkValidity(authHeader);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("is_valid", "true");
            }});
        } catch (Exception e) {
            logger.error("Token is not valid, exception: {}", e.getMessage());
            throw new BussinesRuleException("is_valid", "False", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @return ResponseEntity containing the login response.
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(String username, String password) {
        String loginResponse = restService.login(username, password);
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Logs out a user using the provided refresh token.
     *
     * @param refreshToken the refresh token to log out the user.
     * @return ResponseEntity indicating the logout status.
     * @throws BussinesRuleException if logout fails.
     */
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) throws BussinesRuleException {
        try {
            restService.logout(refreshToken);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("logout", "true");
            }});
        } catch (Exception e) {
            logger.error("Unable to logout, exception: {}", e.getMessage());
            throw new BussinesRuleException("logout", "False", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Refreshes a user's access token using the provided refresh token.
     *
     * @param refreshToken the refresh token for generating a new access token.
     * @return ResponseEntity containing the new access token.
     * @throws BussinesRuleException if refreshing fails.
     */
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refresh(@RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) throws BussinesRuleException {
        try {
            return ResponseEntity.ok(restService.refresh(refreshToken));
        } catch (Exception e) {
            logger.error("Unable to refresh, exception: {}", e.getMessage());
            throw new BussinesRuleException("refresh", "False", HttpStatus.FORBIDDEN);
        }
    }
}
