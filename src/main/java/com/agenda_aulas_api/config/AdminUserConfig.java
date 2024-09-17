package com.agenda_aulas_api.config;

import com.agenda_aulas_api.domain.Role;
import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.domain.UserType;
import com.agenda_aulas_api.repository.RoleRepository;
import com.agenda_aulas_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        User userAdmin = userRepository.findByUsername("admin").orElse(null);

        if (roleAdmin == null) {
            System.err.println("Role ADMIN não encontrada");
            return;
        }

        if (userAdmin != null) {
            System.out.println("admin já existe");
        } else {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRoles(List.of(roleAdmin));
            user.setUserType(UserType.ADMINISTRADOR);
            System.out.println("UserType antes de salvar: " + user.getUserType());
            userRepository.save(user);

        }
    }

}
