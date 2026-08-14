package com.nexora.user.service;

import com.nexora.auth.dto.RegisterRequest;
import com.nexora.common.enums.RoleName;
import com.nexora.common.exception.ValidationException;
import com.nexora.user.dto.UpdateUserProfileRequest;
import com.nexora.user.dto.UserProfileDto;
import com.nexora.user.entity.Role;
import com.nexora.user.entity.User;
import com.nexora.user.repository.RoleRepository;
import com.nexora.user.repository.UserRepository;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ValidationException("Utilisateur introuvable"));
    }

    @Transactional
    public User register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Cet email est déjà utilisé");
        }

        RoleName roleName = request.getRole() != null ? request.getRole() : RoleName.ROLE_CLIENT;
        if (roleName == RoleName.ROLE_ADMIN) {
            throw new ValidationException("Impossible de s'inscrire en tant qu'administrateur");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ValidationException("Rôle invalide"));

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .enabled(true)
                .roles(Set.of(role))
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserProfileDto getMyProfile() {
        return toProfileDto(userContextService.getCurrentUser());
    }

    @Transactional
    public UserProfileDto updateMyProfile(UpdateUserProfileRequest request) {
        User user = userContextService.getCurrentUser();
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        return toProfileDto(userRepository.save(user));
    }

    private UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .build();
    }
}
