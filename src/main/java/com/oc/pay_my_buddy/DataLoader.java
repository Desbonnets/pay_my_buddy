package com.oc.pay_my_buddy;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.repository.UserRepository;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DataLoader(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer des utilisateurs
        User alice = new User();
        alice.setUsername("Alice");
        alice.setEmail("alice@example.com");
        alice.setPassword("password123");

        User bob = new User();
        bob.setUsername("Bob");
        bob.setEmail("bob@example.com");
        bob.setPassword("password123");

        User charlie = new User();
        charlie.setUsername("Charlie");
        charlie.setEmail("charlie@example.com");
        charlie.setPassword("password123");

        // Ajouter des connexions (Many-to-Many)
        alice.setConnections(Set.of(bob, charlie));
        bob.setConnections(Set.of(alice));
        charlie.setConnections(Set.of(alice));

        // Sauvegarder les utilisateurs dans la base
        userRepository.saveAll(List.of(alice, bob, charlie));

        // Créer une transaction entre Alice et Bob
        Transaction transaction = new Transaction();
        transaction.setSender(alice);
        transaction.setReceiver(bob);
        transaction.setDescription("Remboursement du déjeuner");
        transaction.setAmount(25.0);

        // Sauvegarder la transaction dans la base
        transactionRepository.save(transaction);

        System.out.println("Données initiales insérées avec succès !");
    }
}
