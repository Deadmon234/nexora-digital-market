package com.nexora.user.security;

import com.nexora.auth.security.UserPrincipal;
import com.nexora.common.exception.NexoraAuthenticationException;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.user.entity.User;
import com.nexora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserContextService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserPrincipal getCurrentPrincipal() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new NexoraAuthenticationException("Non authentifié");
        }
        return principal;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserPrincipal principal = getCurrentPrincipal();
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentUserOptional() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return Optional.empty();
        }
        return userRepository.findById(principal.getId());
    }
}
