package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllTransactions();
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.insertTransaction(
                transaction.getSender().getId(),
                transaction.getReceiver().getId(),
                transaction.getDescription(),
                transaction.getAmount());
    }

    public List<Transaction> getTransactionsBySenderId(User user) {
        return transactionRepository.findBySenderId(user.getId());
    }
}
