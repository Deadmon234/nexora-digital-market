package com.nexora.auth.security;

import com.nexora.common.exception.NexoraAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtProvider jwtProvider;

    public Claims validateAndGetClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtProvider.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new NexoraAuthenticationException("Token expiré");
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            throw new NexoraAuthenticationException("Token invalide");
        }
    }

    public String extractEmail(String token) {
        return validateAndGetClaims(token).getSubject();
    }

    public boolean isAccessToken(String token) {
        Claims claims = validateAndGetClaims(token);
        Object type = claims.get("type");
        return "access".equals(type);
    }
}
