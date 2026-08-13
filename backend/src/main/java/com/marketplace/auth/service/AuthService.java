package com.marketplace.auth.service;

import com.marketplace.auth.dto.AuthResponse;
import com.marketplace.auth.dto.LoginRequest;
import com.marketplace.auth.dto.RefreshRequest;
import com.marketplace.auth.dto.RegisterRequest;
import com.marketplace.common.exception.BusinessException;
import com.marketplace.security.jwt.JwtService;
import com.marketplace.user.entity.Role;
import com.marketplace.user.entity.User;
import com.marketplace.user.mapper.UserMapper;
import com.marketplace.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       RefreshTokenService refreshTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }

    /** Toute inscription publique cree un CLIENT : les roles SELLER et ADMIN sont attribues par la plateforme. */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Un compte existe deja pour cette adresse e-mail");
        }
        User user = new User(request.email().toLowerCase(), passwordEncoder.encode(request.password()), Role.CLIENT,
                request.firstName(), request.lastName(), request.phone());
        return issueTokens(userRepository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Identifiants invalides");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Compte desactive");
        }
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        User user = refreshTokenService.consume(request.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Jeton de rafraichissement invalide"));
        if (!user.isEnabled()) {
            throw new DisabledException("Compte desactive");
        }
        return issueTokens(user);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenService.revokeAllForUser(userId);
    }

    private AuthResponse issueTokens(User user) {
        return new AuthResponse(jwtService.generateAccessToken(user), refreshTokenService.issue(user),
                jwtService.accessTokenTtlSeconds(), userMapper.toResponse(user));
    }
}
