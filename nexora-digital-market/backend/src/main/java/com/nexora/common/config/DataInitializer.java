package com.nexora.common.config;

import com.nexora.common.enums.RoleName;
import com.nexora.user.entity.Role;
import com.nexora.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    @Profile({"dev", "test"})
    CommandLineRunner initRoles() {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = Role.builder().name(roleName).build();
                    log.info("Création du rôle : {}", roleName);
                    return roleRepository.save(role);
                });
            }
        };
    }
}
