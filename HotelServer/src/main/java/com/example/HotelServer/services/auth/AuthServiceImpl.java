package com.example.HotelServer.services.auth;

import com.example.HotelServer.entity.User;
import com.example.HotelServer.enums.UserRole;
import com.example.HotelServer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void createAnAdminAccount() {
        // Check if an admin user already exists
        Optional<User> adminAccount = userRepository.findByUserRole(UserRole.ADMIN);

        if (adminAccount.isEmpty()) {
            // If no admin account exists, create one
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(passwordEncoder.encode("admin"));

            // Save the new admin user
            userRepository.save(user);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exists");
        }
    }
}
