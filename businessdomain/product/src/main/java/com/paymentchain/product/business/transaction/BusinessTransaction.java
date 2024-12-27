/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.business.transaction;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.exception.BusinessRuleException;
import com.paymentchain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author rvega
 */
@Service
public class BusinessTransaction {

    @Autowired
    ProductRepository productRepository;

    /**
     * This method handles the creation of a new product. It performs validation
     * on the input fields to ensure that both the product code and name are
     * provided and not blank. If either of them is invalid, it throws a
     * BusinessRuleException with appropriate error codes and messages.
     *
     * @param input The product to be created.
     * @return The saved product after successful validation and persistence.
     * @throws BusinessRuleException If any validation fails for product code or
     * name.
     */
    public Product post(Product input) throws BusinessRuleException {
        // Check if the product code is null or blank
        if (input.getCode() == null || input.getCode().isBlank()) {
            // Throw a BusinessRuleException with a specific error code and message for missing product code
            throw new BusinessRuleException(
                    "1045", // Custom error code for missing product code
                    "Validation error, product Code is null or blank", // Error message
                    HttpStatus.PRECONDITION_FAILED // HTTP status indicating the failure
            );
        }

        // Check if the product name is null or blank
        if (input.getName() == null || input.getName().isBlank()) {
            // Throw a BusinessRuleException with a specific error code and message for missing product name
            throw new BusinessRuleException(
                    "1046", // Custom error code for missing product name
                    "Validation error, product Name is null or blank", // Error message
                    HttpStatus.PRECONDITION_FAILED // HTTP status indicating the failure
            );
        }

        // If validation passes, save and return the product
        return productRepository.save(input);
    }

}
