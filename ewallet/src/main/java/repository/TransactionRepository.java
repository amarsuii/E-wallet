package com.app.ewallet.repository;

import com.app.ewallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "t.sender.email = :email OR t.recipient.email = :email " +
            "ORDER BY t.date DESC")
    List<Transaction> findTransactionsForUser(@Param("email") String email);
}
