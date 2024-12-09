/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.paymentchain.customer.repository;

import com.paymentchain.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author rvega
 */

/**
 * Repository interface for performing database operations on the Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Finds a customer by its unique code.
     *
     * @param code the unique code of the customer
     * @return the Customer entity matching the given code, or null if not found
     */
    @Query("SELECT c FROM Customer c WHERE c.code = ?1")
    public Customer findByCode(String code);

    /**
     * Finds a customer by its IBAN (International Bank Account Number).
     *
     * @param iban the IBAN of the customer's account
     * @return the Customer entity matching the given IBAN, or null if not found
     */
    @Query("SELECT c FROM Customer c WHERE c.iban = ?1")
    public Customer findByAccount(String iban);
}
