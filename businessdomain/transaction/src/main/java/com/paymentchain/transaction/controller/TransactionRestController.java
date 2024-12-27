/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.transaction.controller;

import com.paymentchain.transaction.business.transaction.BusinessTransaction;
import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.exception.BusinessRuleException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rvega
 */

/**
 * REST controller that handles HTTP requests related to Transaction entities.
 * Provides methods to create, retrieve, update, and delete transactions.
 */
@RestController  // Indicates that this class is a REST controller handling HTTP requests.
@RequestMapping("/transaction")  // Defines the base URL path for requests to this controller.
public class TransactionRestController {

    /**
     * Automatically injected repository for managing Transaction entities.
     */
    @Autowired
    TransactionRepository transactionRepository;

    // Injects the BusinessTransaction service to handle business logic related to transactions
    @Autowired
    private BusinessTransaction businessTransaction;

    // Logger for logging information and errors in the TransactionRestController class
    private static final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);

    /**
     * Retrieves a list of all transactions.
     *
     * @return A list of all transactions.
     */
    @GetMapping()  // Maps GET requests to /transaction.
    public ResponseEntity<?> List() {
        try {
            // Retrieve all transactions from the repository
            List<Transaction> findAll = transactionRepository.findAll();

            // Check if the list of transactions is empty
            if (findAll.isEmpty()) {
                // Return a 204 No Content response if no transactions are found
                return ResponseEntity.noContent().build();
            } else {
                // Return a 200 OK response with the list of transactions if found
                return ResponseEntity.ok(findAll);
            }
        } catch (Exception e) {
            // Log the error details if an exception occurs during the transaction retrieval process
            logger.error("Error getting all the transactions: ", e.getMessage());

            // Return a 500 Internal Server Error response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }

    }

    /**
     * This method retrieves a transaction by its unique ID. If the transaction
     * is found, it returns a 200 OK response with the transaction data. If no
     * transaction is found for the provided ID, it returns a 404 Not Found
     * response.
     *
     * @param id The ID of the transaction to be retrieved.
     * @return A ResponseEntity containing either the found transaction or a 404
     * Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable(name = "id") long id) {
        // Attempt to find the transaction by ID
        return transactionRepository.findById(id)
                .map(transaction -> {
                    // Return 200 OK if transaction is found
                    return ResponseEntity.ok(transaction);
                })
                .orElseGet(() -> {
                    // Return 404 Not Found if transaction is not found
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves a list of transactions by its iban account. If no transactions
     * are found, returns a 404 Not Found response.
     *
     * @param ibanAccount The iban account for which the transactions are to be
     * retrieved.
     * @return A ResponseEntity containing the list of transactions or 404 Not
     * Found if no transactions are found.
     */
    @GetMapping("/customer/transactions")
    public ResponseEntity<?> get(@RequestParam(name = "ibanAccount") String ibanAccount) {
        try {
            // Retrieve the transactions by ibanAccount
            List<Transaction> transactions = transactionRepository.findByIbanAccount(ibanAccount);

            // Check if the transactions list is not empty
            if (transactions != null && !transactions.isEmpty()) {
                return ResponseEntity.ok(transactions);
            }

            // If no transactions are found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error getting the transaction: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }

    /**
     * Updates an existing transaction with the provided data.
     *
     * @param id The ID of the transaction to update.
     * @param input The transaction object containing updated information.
     * @return The updated transaction.
     */
    @PutMapping("/{id}")  // Maps PUT requests to /transaction/{id}.
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Transaction input) {
        try {
            // Try to find the transaction by ID
            Optional<Transaction> find = transactionRepository.findById(id);

            // If transaction is found, update the fields and save the transaction
            if (find.isPresent()) {
                Transaction existingTransaction = find.get();  // Get the existing transaction object

                // Update the transaction fields with the provided data
                existingTransaction.setAmount(input.getAmount());
                existingTransaction.setChannel(input.getChannel());
                existingTransaction.setDate(input.getDate());
                existingTransaction.setDescription(input.getDescription());
                existingTransaction.setFee(input.getFee());
                existingTransaction.setReference(input.getReference());
                existingTransaction.setStatus(input.getStatus());
                existingTransaction.setIbanAccount(input.getIbanAccount());

                // Save the updated transaction and return the updated object
                Transaction savedTransaction = transactionRepository.save(existingTransaction);
                return ResponseEntity.ok(savedTransaction);  // Return a 200 OK response with the updated transaction
            }
        } catch (Exception e) {
            logger.error("Error updating product: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }

        // If transaction is not found, return a 404 Not Found response
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new transaction.
     *
     * @param input The transaction object to create.
     * @return The created transaction.
     */
    @PostMapping  // Maps POST requests to /transaction.
    public ResponseEntity<?> post(@RequestBody Transaction input) throws BusinessRuleException {
        Transaction save = businessTransaction.post(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete.
     * @return A response indicating whether the deletion was successful.
     */
    @DeleteMapping("/{id}")  // Maps DELETE requests to /transaction/{id}.
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        try {
            Optional<Transaction> findById = transactionRepository.findById(id);  // Check if transaction exists.

            if (findById.isPresent()) {  // Transaction found, proceed with deletion.
                transactionRepository.delete(findById.get());  // Deletes the transaction.
                return ResponseEntity.ok().build();  // Return 200 OK response.
            } else {
                return ResponseEntity.notFound().build();  // Return 404 Not Found if transaction does not exist.
            }
        } catch (Exception e) {
            logger.error("Error deleting transaction: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }
}
