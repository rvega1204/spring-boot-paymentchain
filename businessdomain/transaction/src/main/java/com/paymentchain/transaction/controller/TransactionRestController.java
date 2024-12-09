/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.transaction.controller;

import com.paymentchain.transaction.entities.Transaction;
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
import java.util.NoSuchElementException;
import com.paymentchain.transaction.repository.TransactionRepository;
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

    /**
     * Retrieves a list of all transactions.
     *
     * @return A list of all transactions.
     */
    @GetMapping()  // Maps GET requests to /transaction.
    public List<Transaction> List() {
        return transactionRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable(name = "id") long id) {
         return transactionRepository.findById(id).map(x -> ResponseEntity.ok(x)).orElse(ResponseEntity.notFound().build());      
    }

    /**
     * Retrieves a transaction by its iban account.
     *
     * @return The transaction object with the specified iban account.
     * @throws NoSuchElementException If no transaction is found with the given iban account.
     */
    @GetMapping("/customer/transactions")  // Maps GET requests to /customer/transaction/.
    public List<Transaction> get(@RequestParam(name = "ibanAccount") String ibanAccount) {
        return transactionRepository.findByIbanAccount(ibanAccount);
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
    public ResponseEntity<?> post(@RequestBody Transaction input) {
        Transaction save = transactionRepository.save(input);
        return ResponseEntity.ok(save);  // Return the saved transaction with a 200 OK response.
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete.
     * @return A response indicating whether the deletion was successful.
     */
    @DeleteMapping("/{id}")  // Maps DELETE requests to /transaction/{id}.
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        Optional<Transaction> findById = transactionRepository.findById(id);  // Check if transaction exists.

        if (findById.isPresent()) {  // Transaction found, proceed with deletion.
            transactionRepository.delete(findById.get());  // Deletes the transaction.
            return ResponseEntity.ok().build();  // Return 200 OK response.
        } else {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if transaction does not exist.
        }
    }
}
