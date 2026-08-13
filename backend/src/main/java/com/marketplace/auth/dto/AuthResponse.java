package com.marketplace.auth.dto;

import com.marketplace.user.dto.UserResponse;

public record AuthResponse(String accessToken, String refreshToken, long expiresInSeconds, UserResponse user) {
}
