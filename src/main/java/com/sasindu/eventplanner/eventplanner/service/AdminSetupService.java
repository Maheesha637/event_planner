package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Admin;
import com.sasindu.eventplanner.eventplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminSetupService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createAdminIfNotExists() {
        String email = "admin@example.com";
        if (userRepository.findByEmail(email) == null) {
            Admin admin = new Admin();
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode("admin123")); // Set a secure password!
            admin.setPrivilege("SUPER");
            userRepository.save(admin);
        }
    }
}