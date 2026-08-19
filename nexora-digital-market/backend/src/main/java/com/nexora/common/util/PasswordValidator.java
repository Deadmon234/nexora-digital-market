package com.nexora.common.util;

import com.nexora.common.exception.ValidationException;

public final class PasswordValidator {

    private PasswordValidator() {
    }

    public static void validate(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Le mot de passe doit contenir au moins 8 caractères");
        }
        if (!password.matches(".*[A-Za-z].*")) {
            throw new ValidationException("Le mot de passe doit contenir au moins une lettre");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Le mot de passe doit contenir au moins un chiffre");
        }
    }
}
