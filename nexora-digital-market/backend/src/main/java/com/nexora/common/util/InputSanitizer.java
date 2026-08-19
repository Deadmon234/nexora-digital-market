package com.nexora.common.util;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String sanitizeText(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replaceAll("<[^>]*>", "")
                .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "")
                .trim();
    }

    public static String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.toLowerCase().trim();
    }
}
