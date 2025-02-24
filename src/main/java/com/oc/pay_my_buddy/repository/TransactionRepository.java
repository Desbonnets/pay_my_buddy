package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySender(User sender);
}
