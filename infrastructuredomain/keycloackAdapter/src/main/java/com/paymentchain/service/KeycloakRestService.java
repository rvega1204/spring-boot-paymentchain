/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rvega
 */
/**
 * Service class for interacting with Keycloak for user authentication, token validation,
 * token refresh, and user information retrieval.
 */
@Service
public class KeycloakRestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.user-info-uri}")
    private String keycloakUserInfo;

    @Value("${keycloak.logout}")
    private String keycloakLogout;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.authorization-grant-type}")
    private String grantType;

    @Value("${keycloak.authorization-grant-type-refresh}")
    private String grantTypeRefresh;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.scope}")
    private String scope;

    /**
     * Authenticates a user with Keycloak using their username and password.
     * 
     * @param username the username of the user.
     * @param password the password of the user.
     * @return the token received from Keycloak as a response.
     */
    public String login(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("client_id", clientId);
        map.add("grant_type", grantType);
        map.add("client_secret", clientSecret);
        map.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }

    /**
     * Validates a user token by sending it to Keycloak for verification.
     * 
     * @param token the token to validate.
     * @return user information as a JSON string if the token is valid.
     * @throws Exception if token validation fails.
     */
    public String checkValidity(String token) throws Exception {
        return getUserInfo(token);
    }

    /**
     * Logs out a user by invalidating their active token in Keycloak.
     * 
     * @param refreshToken the refresh token to be invalidated.
     * @throws Exception if logout fails.
     */
    public void logout(String refreshToken) throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        restTemplate.postForObject(keycloakLogout, request, String.class);
    }

    /**
     * Retrieves the roles assigned to a user.
     * 
     * @param token the token of the user.
     * @return a list of roles assigned to the user.
     * @throws Exception if retrieval fails.
     */
    public List<String> getRoles(String token) throws Exception {
        String response = getUserInfo(token);

        // Parse roles from the user information.
        Map map = new ObjectMapper().readValue(response, HashMap.class);
        return (List<String>) map.get("roles");
    }

    /**
     * Sends a request to Keycloak to retrieve user information associated with the token.
     * 
     * @param token the token to fetch user information for.
     * @return user information as a JSON string.
     */
    private String getUserInfo(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        return restTemplate.postForObject(keycloakUserInfo, request, String.class);
    }

    /**
     * Refreshes a user's token by using their refresh token.
     * 
     * @param refreshToken the refresh token to use for generating a new token.
     * @return the new token from Keycloak as a response.
     * @throws Exception if token refresh fails.
     */
    public String refresh(String refreshToken) throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("grant_type", grantTypeRefresh);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }
}

