/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.exception;

import com.paymentchain.customer.common.StandarizedApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author rvega
 */
/**
 * A centralized exception handler for REST APIs. This class ensures consistent
 * error responses for exceptions that occur during API calls.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles generic exceptions and returns a standardized API error response.
     *
     * @param ex the caught exception
     * @return a ResponseEntity containing a standardized error response and an
     * HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleUnknownHostException(Exception ex) {
        // Create a standardized error response with technician-facing details.
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = new StandarizedApiExceptionResponse(
                "TECHNICIAN", // Error audience
                "Input Output error", // Error message
                "1024", // Error code
                ex.getMessage() // Detailed exception message
        );

        // Return the error response with a 500 Internal Server Error status.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standarizedApiExceptionResponse);
    }
    
    /**
     * Handles BusinessRuleException and returns a standardized error response.
     * 
     * @param ex the BusinessRuleException that was thrown
     * @return a ResponseEntity containing the standardized error response and an appropriate HTTP status
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity handleBusinessRuleException(BusinessRuleException ex) {
        // Create a standardized error response with business rule violation details.
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = new StandarizedApiExceptionResponse(
                "BUSINESS", // Error category (can be used for categorization)
                "Validation error", // Generic error message
                ex.getCode(), // Custom code for the business rule violation
                ex.getMessage() // Detailed message from the exception
        );

        // Return the error response with the appropriate HTTP status (set in the BusinessRuleException).
        return ResponseEntity.status(ex.getHttpStatus()).body(standarizedApiExceptionResponse);
    }
}
