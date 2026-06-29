package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("All fields (First Name, Last Name, Email, Password) are required");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}