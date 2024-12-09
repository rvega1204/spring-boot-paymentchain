/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.entities;

import com.github.javafaker.Code;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.Data;

/**
 *
 * @author rvega
 */
@Data  // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods
@Entity  // JPA annotation to indicate that this class is an entity and will be mapped to a database table
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)  // Automatically generates the ID value for this entity
    @Id  // JPA annotation to specify the primary key of the entity
    private long id;  // Unique identifier for the Customer
    
    private String name;  // Name of the customer
    
    private String phone;  // Phone number of the customer
    
    private String iban; // Iban of the customer
    
    private String surname; // Customer last name
    
    private String address; // Customer address
    
    private String code; // Customer code
    
    // One-to-many relationship: products are loaded lazily, cascaded, and removed if orphaned
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    
    // List of products associated with the customer (managed by JPA)
    private List<CustomerProduct> products;
    
    // Transient field: not persisted in the database
    @Transient
    private List<?> transactions;
    
}
