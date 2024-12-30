/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import com.paymentchain.customer.business.transactions.BusinnesTransaction;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import java.net.UnknownHostException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rvega
 */
/**
 * REST controller that handles HTTP requests related to Customer entities.
 * Provides methods to create, retrieve, update, and delete customers.
 */
@RestController  // Indicates that this class is a REST controller handling HTTP requests.
@RequestMapping("/customer/v1")  // Defines the base URL path for requests to this controller.
public class CustomerRestController {

    /**
     * Automatically injected repository for managing Customer entities.
     */
    @Autowired
    CustomerRepository customerRepository;

    /**
     * Logger instance for this class. Used to log information, warnings, and
     * errors during execution.
     */
    private static final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    /**
     * Environment object to access application properties.
     */
    @Autowired
    private Environment env;

    /**
     * Business logic layer dependency for handling transactions.
     */
    @Autowired
    private BusinnesTransaction businnesTransaction;

    /**
     * A GET endpoint to check if the application is running and display a
     * custom property value.
     *
     * @return A greeting message along with the value of the
     * "custom.activeprofileName" property.
     */
    @GetMapping("/check")
    public String check() {
        // Fetch the custom property from the environment and return it with a greeting.
        return "Hello, your property value is: " + env.getProperty("custom.activeprofileName");
    }

    /**
     * Retrieves a list of all customers.
     *
     * @return A list of all customers.
     */
    @GetMapping()  // Maps GET requests to /customer.
    public ResponseEntity<?> List() {
        try {
            List<Customer> findAll = customerRepository.findAll();  // Returns all customers from the database.
            // Check if the list of customers is empty
            if (findAll.isEmpty()) {
                // If no customers are found, return HTTP status 204 NO CONTENT
                return ResponseEntity.noContent().build();
            } else {
                // If customers are found, return the list with HTTP status 200 OK
                return ResponseEntity.ok(findAll);
            }
        } catch (Exception e) {
            // Log the error for troubleshooting
            logger.error("Error getting all customers: ", e.getMessage());

            // Optionally, return a 500 INTERNAL SERVER ERROR if an exception occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id the unique identifier of the customer
     * @return a ResponseEntity containing the customer data if found, or a 404
     * NOT FOUND status otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(name = "id") long id) {
        // Attempt to retrieve the customer by ID from the repository
        Optional<Customer> findById = customerRepository.findById(id);

        // If the customer is found, return the customer data with HTTP status 200 OK
        if (findById.isPresent()) {
            return ResponseEntity.ok(findById.get());
        } else {
            // If the customer is not found, return HTTP status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Updates an existing customer with the provided data.
     *
     * @param id The ID of the customer to update.
     * @param input The customer object containing updated information.
     * @return The updated customer.
     */
    @PutMapping("/{id}")  // Maps PUT requests to /customer/{id}.
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Customer input) {
        try {
            // Find the customer by ID
            Optional<Customer> find = customerRepository.findById(id);

            // If customer is found, update the fields and save the customer
            if (find.isPresent()) {
                Customer existingCustomer = find.get();  // Get the customer object

                // Update customer fields with the provided data
                existingCustomer.setAddress(input.getAddress());
                existingCustomer.setCode(input.getCode());
                existingCustomer.setIban(input.getIban());
                existingCustomer.setName(input.getName());
                existingCustomer.setPhone(input.getPhone());
                existingCustomer.setSurname(input.getSurname());

                // Save the updated customer and return the updated object
                Customer savedCustomer = customerRepository.save(existingCustomer);
                return ResponseEntity.ok(savedCustomer);  // Return a 200 OK response with the updated customer
            }
        } catch (Exception e) {
            // Log the error with the customer ID for troubleshooting
            logger.error("Error updating customer with ID: {}", id, e);

            // Optionally, return a 500 INTERNAL SERVER ERROR if an exception occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }

        // If customer is not found, return a 404 Not Found response
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new customer using the provided input and returns the created
     * customer.
     *
     * @param input the customer data to be created
     * @return a ResponseEntity containing the created customer and HTTP status
     * 201 Created
     * @throws BusinessRuleException if any business rules are violated during
     * customer creation
     */
    @PostMapping  // Maps POST requests to /customer.
    public ResponseEntity<?> post(@RequestBody Customer input) throws BusinessRuleException, UnknownHostException, InterruptedException {
        // Attempt to create a new customer by passing the input to the business transaction layer
        Customer post = businnesTransaction.post(input);

        // Return the created customer with HTTP status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return A response indicating whether the deletion was successful.
     */
    @DeleteMapping("/{id}")  // Maps DELETE requests to /customer/{id}.
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        Optional<Customer> findById = customerRepository.findById(id);  // Check if customer exists.

        if (findById.isPresent()) {  // Customer found, proceed with deletion.
            customerRepository.delete(findById.get());  // Deletes the customer.
            return ResponseEntity.ok().build();  // Return 200 OK response.
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 Not Found if customer does not exist.
        }
    }

    /**
     * Retrieves a customer by their unique code.
     *
     * @param code the unique code identifying the customer
     * @return the customer associated with the given code
     */
    @GetMapping("/byCode/{code}")
    public Customer getByCode(@PathVariable(name = "code") String code) {
        // Use the business transaction layer to retrieve the customer by code
        try {
            Customer customer = businnesTransaction.get(code);
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the retrieved customer
        return null;
    }
}
