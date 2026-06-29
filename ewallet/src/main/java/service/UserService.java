package com.app.ewallet.service;

import com.app.ewallet.model.Transaction;
import com.app.ewallet.model.User;
import com.app.ewallet.repository.TransactionRepository;
import com.app.ewallet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // Register user (duplicate email protected)
    public User saveUser(User user) {
        User exist = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (exist != null) {
            return null; // block duplicate email
        }
        user.setBalance(0.0);
        return userRepository.save(user);
    }

    // Login
    public User login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(email);
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }

    // Add money
    public void addMoney(String email, double amount) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null || amount <= 0) return;

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction txn = new Transaction(user, user, "RECHARGE", amount, LocalDateTime.now());
        transactionRepository.save(txn);
    }

    // Transfer
    public boolean transfer(String senderEmail, String receiverEmail, double amount) {
        User sender = userRepository.findByEmailIgnoreCase(senderEmail);
        User receiver = userRepository.findByEmailIgnoreCase(receiverEmail);

        if (sender == null || receiver == null || amount <= 0 || sender.getBalance() < amount)
            return false;

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        userRepository.save(sender);
        userRepository.save(receiver);

        transactionRepository.save(new Transaction(sender, receiver, "SENT", amount, LocalDateTime.now()));
        transactionRepository.save(new Transaction(sender, receiver, "RECEIVED", amount, LocalDateTime.now()));

        return true;
    }

    public double getBalance(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        return user != null ? user.getBalance() : 0.0;
    }

    public List<Transaction> getHistory(String email) {
        return transactionRepository.findTransactionsForUser(email);
    }
}
