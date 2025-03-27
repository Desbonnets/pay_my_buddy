package com.oc.pay_my_buddy.modele;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialiser le validateur
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testTransactionValid() {
        // Création d'un utilisateur factice pour le sender et le receiver
        User sender = new User();
        sender.setEmail("sender@example.com");
        User receiver = new User();
        receiver.setEmail("receiver@example.com");

        // Création d'une transaction valide
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription("Payment for services");
        transaction.setAmount(100.0);

        // Validation de la transaction
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertTrue(violations.isEmpty(), "La transaction ne doit pas contenir d'erreurs de validation.");
    }

    @Test
    void testTransactionInvalidDescription() {
        // Création d'un utilisateur factice pour le sender et le receiver
        User sender = new User();
        sender.setEmail("sender@example.com");
        User receiver = new User();
        receiver.setEmail("receiver@example.com");

        // Création d'une transaction invalide sans description
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(""); // Description vide
        transaction.setAmount(100.0);

        // Validation de la transaction
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "La transaction doit contenir des erreurs de validation.");
        assertEquals(1, violations.size(), "Il devrait y avoir une seule erreur de validation.");
        assertEquals("La description est obligatoire", violations.iterator().next().getMessage());
    }

    @Test
    void testTransactionInvalidSenderReceiver() {
        // Transaction avec sender ou receiver null
        Transaction transaction = new Transaction();
        transaction.setReceiver(null); // Receiver null
        transaction.setDescription("Payment for services");
        transaction.setAmount(100.0);

        // Validation de la transaction
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "La transaction doit contenir des erreurs de validation.");
        assertEquals(1, violations.size(), "Il devrait y avoir deux erreurs de validation.");
    }

    @Test
    void testTransactionInvalidAmount() {
        // Création d'une transaction valide
        User sender = new User();
        sender.setEmail("sender@example.com");
        User receiver = new User();
        receiver.setEmail("receiver@example.com");

        // Transaction avec montant invalide (par exemple, négatif ou nul)
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription("Payment for services");
        transaction.setAmount(-50.0); // Montant invalide

        // Validation de la transaction
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "La transaction doit contenir des erreurs de validation.");
    }
}
