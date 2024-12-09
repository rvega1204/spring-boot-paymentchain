/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author rvega
 */
@Data  // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods
@Entity  // JPA annotation to indicate that this class is an entity and will be mapped to a database table
public class Product {
    @GeneratedValue(strategy = GenerationType.AUTO)  // Automatically generates the ID value for this entity
    @Id  // JPA annotation to specify the primary key of the entity
    private long id;  // Unique identifier for the Product
    
    private String code;  // Code of the product
    
    private String name;  // Name of the product
    
}
