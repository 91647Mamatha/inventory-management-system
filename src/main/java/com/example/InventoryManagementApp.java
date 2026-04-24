package com.example;

import com.example.inventory.entity.User;
import com.example.inventory.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InventoryManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApp.class, args);
    }

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(new User("admin",
                        encoder.encode("admin123"), "ADMIN", true));
            }
            if (userRepo.findByUsername("user").isEmpty()) {
                userRepo.save(new User("user",
                        encoder.encode("user123"), "USER", true));
            }
        };
    }
}
