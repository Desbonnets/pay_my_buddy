package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transaction", nativeQuery = true)
    List<Transaction> findAllTransactions();

    @Query(value = "SELECT * FROM transaction WHERE sender = :senderId", nativeQuery = true)
    List<Transaction> findBySenderId(@Param("senderId") int senderId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO transaction(sender, receiver, description, amount) " +
            "VALUES (:senderId, :receiverId, :description, :amount)", nativeQuery = true)
    void insertTransaction(
            @Param("senderId") int senderId,
            @Param("receiverId") int receiverId,
            @Param("description") String description,
            @Param("amount") double amount);
//    List<Transaction> findBySender(User sender);
}
