package com.marketplace.security.jwt;

import com.marketplace.user.entity.Role;

public record JwtPrincipal(Long userId, Role role) {
}
