package com.marketplace.user.entity;

public enum Role {
    CLIENT,
    SELLER,
    ADMIN;

    public String authority() {
        return "ROLE_" + name();
    }
}
