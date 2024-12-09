/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 *
 * @author rvega
 */

/**
 * Represents a custom exception for business rule violations. This class
 * encapsulates details about the error, including an ID, a custom code, and an
 * associated HTTP status.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessRuleException extends Exception {

    /**
     * Unique identifier for the exception.
     */
    private long id;

    /**
     * Custom code representing the specific business rule violation.
     */
    private String code;

    /**
     * HTTP status code associated with this exception.
     */
    private HttpStatus httpStatus;

    /**
     * Constructor for creating a BusinessRuleException with a unique ID, 
     * business rule code, message, and HTTP status.
     *
     * @param id the unique identifier for the exception
     * @param code the custom business rule violation code
     * @param message the detailed message describing the exception
     * @param httpStatus the HTTP status associated with this exception
     */
    public BusinessRuleException(long id, String code, String message, HttpStatus httpStatus) {
        super(message); // Pass the message to the superclass (Exception)
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor for creating a BusinessRuleException with a business rule code,
     * message, and HTTP status (without an ID).
     *
     * @param code the custom business rule violation code
     * @param message the detailed message describing the exception
     * @param httpStatus the HTTP status associated with this exception
     */
    public BusinessRuleException(String code, String message, HttpStatus httpStatus) {
        super(message); // Pass the message to the superclass (Exception)
        this.code = code;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor for creating a BusinessRuleException with a message and cause.
     * This is typically used for wrapping another throwable (e.g., a lower-level exception).
     *
     * @param message the detailed message describing the exception
     * @param cause the underlying cause of the exception
     */
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause); // Pass message and cause to the superclass (Exception)
    }

}
