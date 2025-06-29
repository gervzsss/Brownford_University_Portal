package com.brownford.config;

import com.brownford.model.User;
import com.brownford.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminDataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if an admin user exists
        if (userRepository.findByRoleIgnoreCase("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setFirstName("System");
            admin.setMiddleName("Account");
            admin.setLastName("Administrator");
            admin.setEmail("admin@brownford.edu");
            admin.setRole("admin");
            admin.setStatus("active");
            admin.setPassword(passwordEncoder.encode("admin")); // Default password
            userRepository.save(admin);
            System.out.println("[INFO] Default admin user created: username=admin, password=admin");
        }
    }
}
