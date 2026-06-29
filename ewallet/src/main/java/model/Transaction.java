package com.app.ewallet.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    private String type; // RECHARGE / SENT / RECEIVED
    private double amount;
    private LocalDateTime date;

    public Transaction() {}

    public Transaction(User sender, User recipient, String type, double amount, LocalDateTime date) {
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
