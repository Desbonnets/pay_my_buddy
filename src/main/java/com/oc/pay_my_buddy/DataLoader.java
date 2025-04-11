package com.oc.pay_my_buddy;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.repository.UserRepository;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insertion des utilisateurs avec requêtes SQL personnalisées
        userRepository.insertUser("Alice", "alice@example.com", passwordEncoder.encode("pass"));
        userRepository.insertUser("Bob", "bob@example.com", passwordEncoder.encode("pass"));
        userRepository.insertUser("Charlie", "charlie@example.com", passwordEncoder.encode("pass"));

        // Récupération des utilisateurs pour créer les objets User
        User alice = userRepository.findByEmail("alice@example.com").orElseThrow();
        User bob = userRepository.findByEmail("bob@example.com").orElseThrow();
        User charlie = userRepository.findByEmail("charlie@example.com").orElseThrow();

//        alice.addConnection(charlie);
//        bob.addConnection(charlie);

        userRepository.updateUser(alice.getId(), alice.getUsername(), alice.getEmail(), alice.getPassword());
        userRepository.updateUser(charlie.getId(), charlie.getUsername(), charlie.getEmail(), charlie.getPassword());

        // Créer une transaction entre Alice et Charlie
        Transaction transaction = new Transaction();
        transaction.setSender(alice);
        transaction.setReceiver(charlie);
        transaction.setDescription("Remboursement du déjeuner");
        transaction.setAmount(25.0);

        transactionRepository.insertTransaction(transaction.getSender().getId(), transaction.getReceiver().getId(), transaction.getDescription(), transaction.getAmount());

        logger.info("Données initiales insérées avec succès !");
    }
}
