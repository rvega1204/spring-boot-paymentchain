/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.paymentchain.customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 *
 * @author rvega
 */

/**
 * Represents a product associated with a customer.
 * This entity is part of the many-to-one relationship with the Customer entity.
 */
@Data
@Entity
public class CustomerProduct {

    /**
     * Unique identifier for the CustomerProduct entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * The ID of the product associated with this customer product.
     */
    private long productId;

    /**
     * Temporary field for the product's name, not persisted in the database.
     * This field is only used during runtime.
     */
    @Transient
    private String productName;

    /**
     * The customer associated with this product.
     * This is a many-to-one relationship, with lazy loading.
     * The customer ID is stored in the 'customerId' column in the database.
     */
    @JsonIgnore  // Prevents serializing the 'customer' field in JSON responses
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customerId", nullable = true)
    private Customer customer;

}
