package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Integer userID) {
        userRepository.deleteById(userID);
    }

    public User createUser(String firstName, String lastName, String email, String password) {
        // Check if user with email already exists
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        return userRepository.save(user);
    }

    // Placeholder for backup (simplified)
    public String backupData() {
        return "Backup performed (simulated)"; // Implement actual backup logic
    }
}