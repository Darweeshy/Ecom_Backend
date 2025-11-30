package org.example.springecom.service;

import org.example.springecom.model.User;
import org.example.springecom.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public User saveUser(User user) {
        // Duplicate checks
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (repo.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Password strength validation
        String password = user.getPassword();
        if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters, include uppercase, lowercase, and a number");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        User savedUser = repo.save(user);

        // Send welcome email after user is successfully saved
        emailService.sendWelcomeEmail(savedUser);

        return savedUser;
    }

    public User getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    public void generatePasswordResetToken(String userEmail) {
        User user = repo.findByEmail(userEmail);
        if (user == null) {
            System.out.println("Password reset request for non-existent email: " + userEmail);
            return;
        }

        String token = UUID.randomUUID().toString();
        Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        user.setResetToken(token);
        user.setResetTokenExpiryDate(expiryDate);
        repo.save(user);

        // Send the password reset email containing the token and link
        emailService.sendPasswordResetEmail(user, token);
    }

    public void resetPassword(String token, String newPassword) {
        User user = repo.findByResetToken(token);
        if (user == null || user.getResetTokenExpiryDate().before(new Date())) {
            throw new RuntimeException("Invalid or expired password reset token.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiryDate(null);
        repo.save(user);
    }
}