/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.paymentchain.transaction.business.transaction;

import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.exception.BusinessRuleException;
import com.paymentchain.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author rvega
 */
@Service
public class BusinessTransaction {
    
    /**
     * Automatically injected repository for managing Transaction entities.
     */
    @Autowired
    TransactionRepository transactionRepository;
    
    public Transaction post(Transaction input) throws BusinessRuleException {
        if (input.getIbanAccount().isBlank() || input.getIbanAccount() == null) {
            // Throw a BusinessRuleException with a specific error code and message
            throw new BusinessRuleException(
                    "1055", // Custom error code
                    "Validation error, transaction Iban Account is null or blank", // Error message
                    HttpStatus.PRECONDITION_FAILED // HTTP status indicating the failure
            );
        }
        
        if (input.getStatus().isBlank() || input.getStatus() == null) {
            // Throw a BusinessRuleException with a specific error code and message
            throw new BusinessRuleException(
                    "1056", // Custom error code
                    "Validation error, transaction Status is null or blank", // Error message
                    HttpStatus.PRECONDITION_FAILED // HTTP status indicating the failure
            );
        }
        
        return transactionRepository.save(input);
    }

}
