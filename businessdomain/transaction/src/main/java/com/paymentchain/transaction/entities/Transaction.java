/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author rvega
 */

/**
 * Entity class representing a transaction in the system.
 * This class is mapped to a database table for storing transaction details.
 */
@Data  // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods
@Entity  // JPA annotation to indicate that this class is an entity and will be mapped to a database table
public class Transaction {
    @GeneratedValue(strategy = GenerationType.AUTO)  // Automatically generates the ID value for this entity
    @Id  // JPA annotation to specify the primary key of the entity
    private long id;  // Unique identifier for the Transaction
    
    private String reference;  // Reference of the transaction
    
    private String ibanAccount;  // Account IBAN associated with the transaction
    
    private LocalDateTime date;  // Date of the transaction
    
    private double amount;  // Amount involved in the transaction
    
    private double fee;  // Fee associated with the transaction
    
    private String description;  // Description of the transaction
    
    private String status;  // Status of the transaction (e.g., completed, pending, failed)
    
    private String channel;  // Channel through which the transaction was processed (e.g., online, mobile app, etc.)
}
