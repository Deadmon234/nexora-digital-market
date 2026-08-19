package com.nexora.auth.service;

import com.nexora.auth.config.JwtProperties;
import com.nexora.auth.dto.AuthResponse;
import com.nexora.auth.dto.LoginRequest;
import com.nexora.auth.dto.RegisterRequest;
import com.nexora.auth.entity.RefreshToken;
import com.nexora.auth.repository.RefreshTokenRepository;
import com.nexora.auth.security.JwtProvider;
import com.nexora.auth.security.UserPrincipal;
import com.nexora.common.enums.RoleName;
import com.nexora.common.exception.NexoraAuthenticationException;
import com.nexora.common.exception.ValidationException;
import com.nexora.security.audit.AuditService;
import com.nexora.user.entity.User;
import com.nexora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = userService.register(request);
        auditService.log("REGISTER", user.getEmail(), true, "Inscription réussie");
        return buildAuthResponse(new UserPrincipal(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase().trim(),
                            request.getPassword()
                    )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            refreshTokenRepository.revokeAllByUser(
                    userService.findByEmail(principal.getEmail())
            );
            auditService.log("LOGIN", principal.getEmail(), true, "Connexion réussie");
            return buildAuthResponse(principal);
        } catch (Exception ex) {
            auditService.log("LOGIN", request.getEmail(), false, "Échec de connexion");
            throw ex;
        }
    }

    @Transactional
    public AuthResponse refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new NexoraAuthenticationException("Refresh token invalide"));

        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new NexoraAuthenticationException("Refresh token expiré ou révoqué");
        }

        User user = refreshToken.getUser();
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return buildAuthResponse(new UserPrincipal(user));
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            auditService.log("LOGOUT", token.getUser().getEmail(), true, "Déconnexion");
        });
    }

    private AuthResponse buildAuthResponse(UserPrincipal principal) {
        String accessToken = jwtProvider.generateAccessToken(principal);
        String refreshTokenValue = jwtProvider.generateRefreshTokenValue();

        User user = userService.findByEmail(principal.getEmail());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpirationMs() / 1000))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessExpirationMs() / 1000)
                .user(AuthResponse.UserInfo.builder()
                        .id(principal.getId())
                        .email(principal.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .roles(user.getRoles().stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet()))
                        .build())
                .build();
    }
}
