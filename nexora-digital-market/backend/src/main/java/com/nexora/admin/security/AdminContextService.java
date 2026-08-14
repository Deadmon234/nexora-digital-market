package com.nexora.admin.security;

import com.nexora.auth.security.UserPrincipal;
import com.nexora.common.enums.RoleName;
import com.nexora.common.exception.ValidationException;
import com.nexora.user.entity.User;
import com.nexora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminContextService {

    private final UserRepository userRepository;

    public UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new ValidationException("Utilisateur non authentifié");
        }
        return principal;
    }

    public User requireAdmin() {
        UserPrincipal principal = getCurrentUser();
        if (!principal.hasRole(RoleName.ROLE_ADMIN)) {
            throw new ValidationException("Accès réservé aux administrateurs");
        }
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ValidationException("Utilisateur introuvable"));
    }
}
