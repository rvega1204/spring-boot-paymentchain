/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.paymentchain.transaction.repository;

import com.paymentchain.transaction.entities.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author rvega
 */

/**
 * Repository interface for managing {@link Transaction} entities.
 * Extends JpaRepository to provide basic CRUD operations and custom queries.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Custom query to find a {@link Transaction} by its associated IBAN account.
     *
     * @param ibanAccount the IBAN account to search for.
     * @return the {@link Transaction} associated with the given IBAN account, or null if not found.
     */
    @Query("SELECT t FROM Transaction t WHERE t.ibanAccount = ?1")
    public List<Transaction> findByIbanAccount(String ibanAccount);

}