package com.oc.pay_my_buddy.modele;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private Set<User> connections;

    @ManyToMany(mappedBy = "connections")
    private Set<User> connectedBy;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsReceived;

    // Constructeur par défaut
    public User() {}

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getConnections() {
        return connections;
    }

    public void setConnections(Set<User> connections) {
        this.connections = connections;
    }

    public Set<User> getConnectedBy() {
        return connectedBy;
    }

    public void setConnectedBy(Set<User> connectedBy) {
        this.connectedBy = connectedBy;
    }

    public List<Transaction> getTransactionsSent() {
        return transactionsSent;
    }

    public void setTransactionsSent(List<Transaction> transactionsSent) {
        this.transactionsSent = transactionsSent;
    }

    public List<Transaction> getTransactionsReceived() {
        return transactionsReceived;
    }

    public void setTransactionsReceived(List<Transaction> transactionsReceived) {
        this.transactionsReceived = transactionsReceived;
    }
}
