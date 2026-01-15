package com.agenda_aulas_api.config;

import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.domain.UserType;
import com.agenda_aulas_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.transaction.Transactional;


@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setUserType(UserType.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin (com UserType) criado com sucesso!");
        } else {
            System.out.println("Admin já existe.");
        }
    }
}