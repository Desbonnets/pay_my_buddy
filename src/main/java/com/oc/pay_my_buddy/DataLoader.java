package com.oc.pay_my_buddy;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.repository.UserRepository;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import com.oc.pay_my_buddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger= LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer des utilisateurs
        User alice = new User();
        alice.setUsername("Alice");
        alice.setEmail("alice@example.com");
        alice.setPassword(passwordEncoder.encode("pass"));

        User bob = new User();
        bob.setUsername("Bob");
        bob.setEmail("bob@example.com");
        bob.setPassword(passwordEncoder.encode("pass"));

        User charlie = new User();
        charlie.setUsername("Charlie");
        charlie.setEmail("charlie@example.com");
        charlie.setPassword(passwordEncoder.encode("pass"));

        // Ajouter des connexions (Many-to-Many)
        alice.addConnection(charlie);

        // Sauvegarder les utilisateurs dans la base
        userRepository.saveAll(List.of(alice, bob, charlie));

        // Créer une transaction entre Alice et Bob
        Transaction transaction = new Transaction();
        transaction.setSender(alice);
        transaction.setReceiver(charlie);
        transaction.setDescription("Remboursement du déjeuner");
        transaction.setAmount(25.0);

        // Sauvegarder la transaction dans la base
        transactionRepository.save(transaction);

        logger.info("Données initiales insérées avec succès !");
    }
}
