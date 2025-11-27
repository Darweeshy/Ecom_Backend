package org.example.springecom.controller;

import org.example.springecom.model.LoginResponse;
import org.example.springecom.model.User;
import org.example.springecom.service.JwtService;
import org.example.springecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired private UserService service;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        // FIX: Added try-catch to handle bad credentials gracefully.
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if(authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());
                String role = service.getUserByUsername(user.getUsername()).getRole();
                return ResponseEntity.ok(new LoginResponse(token, role));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        service.generatePasswordResetToken(email);
        return ResponseEntity.ok(Map.of("message", "If an account with that email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("password");
        try {
            service.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}