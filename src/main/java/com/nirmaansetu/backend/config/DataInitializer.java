package com.nirmaansetu.backend.config;

import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component to initialize default data in the database on application startup.
 * Specifically, it ensures a SUPER_ADMIN user exists.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes logic on startup to seed initial data.
     */
    @Override
    public void run(String... args) throws Exception {
        // Create a default SUPER_ADMIN if none exists
        if (userRepository.findByRole(Role.SUPER_ADMIN).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setPhoneNumber("+919999999999");
            superAdmin.setName("Super Admin");
            superAdmin.setAadhaarNumber("000000000000");
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            
            userRepository.save(superAdmin);
            log.info("Default SUPER_ADMIN user created with phone: +919999999999 and password: Admin@123");
        }
    }
}
