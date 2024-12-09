/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
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
import com.paymentchain.product.repository.ProductRepository;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;

/**
 *
 * @author rvega
 */

/**
 * REST controller that handles HTTP requests related to Product entities.
 * Provides methods to create, retrieve, update, and delete products.
 */
@RestController  // Indicates that this class is a REST controller handling HTTP requests.
@RequestMapping("/product")  // Defines the base URL path for requests to this controller.
public class ProductRestController {

    /**
     * Automatically injected repository for managing Product entities.
     */
    @Autowired
    ProductRepository productRepository;

    /**
     * Retrieves a list of all products.
     *
     * @return A list of all products.
     */
    @GetMapping()  // Maps GET requests to /product.
    public List<Product> List() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product object with the specified ID.
     * @throws NoSuchElementException If no product is found with the given ID.
     */
    @GetMapping("/{id}")  // Maps GET requests to /product/{id}.
    public ResponseEntity<Optional<Product>> get(@PathVariable("id") long id) {
        Optional<Product> findById = productRepository.findById(id);
        if (findById.isPresent()) {
            return ResponseEntity.ok(findById);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Updates an existing product with the provided data.
     *
     * @param id The ID of the product to update.
     * @param input The product object containing updated information.
     * @return The updated product.
     */
    @PutMapping("/{id}")  // Maps PUT requests to /product/{id}.
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody Product input) {
        // Try to find the product by ID
        Optional<Product> find = productRepository.findById(id);

        // If product is found, update the fields and save the product
        if (find.isPresent()) {
            Product existingProduct = find.get();  // Get the existing product object

            // Update the product fields with the provided data
            existingProduct.setCode(input.getCode());
            existingProduct.setName(input.getName());

            // Save the updated product and return the updated object
            Product savedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(savedProduct);  // Return a 200 OK response with the updated product
        }

        // If product is not found, return a 404 Not Found response
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new product.
     *
     * @param input The product object to create.
     * @return The created product.
     */
    @PostMapping  // Maps POST requests to /product.
    public ResponseEntity<?> post(@RequestBody Product input) {
        Product save = productRepository.save(input);
        return ResponseEntity.ok(save);  // Return the saved product with a 200 OK response.
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A response indicating whether the deletion was successful.
     */
    @DeleteMapping("/{id}")  // Maps DELETE requests to /product/{id}.
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Optional<Product> findById = productRepository.findById(id);  // Check if product exists.

        if (findById.isPresent()) {  // Product found, proceed with deletion.
            productRepository.delete(findById.get());  // Deletes the product.
            return ResponseEntity.ok().build();  // Return 200 OK response.
        } else {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if product does not exist.
        }
    }
}
