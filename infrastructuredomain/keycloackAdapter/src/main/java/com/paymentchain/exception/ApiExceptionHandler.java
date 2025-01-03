/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.exception;


import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author rvega
 * Standard http communication have five levels of response codes
 * 100-level (Informational) — Server acknowledges a request, it mean that request was received and understood, it is transient response , alert client for awaiting response
 * 200-level (Success) — Server completed the request as expected
 * 300-level (Redirection) — Client needs to perform further actions to complete the request
 * 400-level (Client error) — Client sent an invalid request
 * 500-level (Server error) — Server failed to fulfill a valid request due to an error with server
 * 
 * The goal of handler exception is provide to customer with appropriate code and 
 * additional comprehensible information to help troubleshoot the problem. 
 * The message portion of the body should be present as user interface, event if 
 * customer send an Accept-Language header (en or french ie) we should translate the title part 
 * to customer language if we support internationalization, detail is intended for developer
 * of clients, so the translation is not necessary. If more than one error is need to report , we can 
 * response a list of errors.
 * 
 */
/**
 * Global exception handler for REST controllers.
 * Provides standardized responses for specific exceptions.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles UnknownHostException by returning a standardized error response.
     *
     * @param ex the exception thrown when the host is unknown.
     * @return ResponseEntity with a standardized API exception response and HTTP status PARTIAL_CONTENT.
     */
    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleUnknownHostException(UnknownHostException ex) {
        // Create a standardized response for connection errors
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse(
            "Connection error", // Error message
            "error-1024",      // Error code
            ex.getMessage()     // Exception details
        );
        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * Handles BussinesRuleException by returning a standardized error response.
     *
     * @param ex the exception thrown when a business rule is violated.
     * @return ResponseEntity with a standardized API exception response and the exception's HTTP status.
     */
    @ExceptionHandler(BussinesRuleException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleBussinesRuleException(BussinesRuleException ex) {
        // Create a standardized response for validation errors
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse(
            "Validation error", // Error message
            ex.getCode(),        // Error code
            ex.getMessage()      // Exception details
        );
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
}
