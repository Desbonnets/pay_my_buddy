package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySender(User sender);
}
