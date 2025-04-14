package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private User sender;

    @BeforeEach
    void setUp() {
        sender = new User();
        transaction = new Transaction();
        transaction.setSender(sender);
    }

    @Test
    void getAllTransactions_ShouldReturnTransactionList() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void createTransaction_ShouldSaveAndReturnTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.createTransaction(transaction);
        assertNotNull(result);
        assertEquals(transaction, result);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void getTransactionsBySenderId_ShouldReturnTransactions() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findBySender(sender)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionsBySenderId(sender);
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));
        verify(transactionRepository, times(1)).findBySender(sender);
    }
}
