package com.ablue.ecommerce;

import com.ablue.ecommerce.model.Role;
import com.ablue.ecommerce.model.User;
import com.ablue.ecommerce.repository.RoleRepository;
import com.ablue.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Slf4j
@SpringBootApplication
public class AblueItApplication {

    public static void main(String[] args) {
        SpringApplication.run(AblueItApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            log.info("check ROLE_ADMIN exist");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                return roleRepository.save(newRole); // Lưu Role vào DB trước
            });

            // Kiểm tra xem user "admin" đã tồn tại chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setRoles(Collections.singleton(adminRole)); // Gán role đã lưu);
                userRepository.save(admin);
                log.warn("Admin account has been created with default password : admin123, please change your password");
            }

            log.info("application initialization completed....");
        };
    }


}
