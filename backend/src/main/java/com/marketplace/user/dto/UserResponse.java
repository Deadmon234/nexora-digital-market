package com.marketplace.user.dto;

import com.marketplace.user.entity.Role;

public record UserResponse(Long id, String email, Role role, String firstName, String lastName, String phone) {
}
