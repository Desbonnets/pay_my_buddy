package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
