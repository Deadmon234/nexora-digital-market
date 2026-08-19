package com.nexora.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "nexora.security.rate-limit.enabled", havingValue = "true", matchIfMissing = true)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String[] LIMITED_PATHS = {
            "/api/auth/login",
            "/api/auth/register"
    };

    private final ObjectMapper objectMapper;

    @Value("${nexora.security.rate-limit.max-requests:20}")
    private int maxRequests;

    @Value("${nexora.security.rate-limit.window-seconds:60}")
    private int windowSeconds;

    private final ConcurrentHashMap<String, Window> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!"POST".equalsIgnoreCase(request.getMethod()) || !isLimitedPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = resolveClientIp(request) + ":" + request.getRequestURI();
        Window window = buckets.computeIfAbsent(key, k -> new Window());

        synchronized (window) {
            long now = Instant.now().getEpochSecond();
            if (now - window.startEpochSecond >= windowSeconds) {
                window.startEpochSecond = now;
                window.count.set(0);
            }

            if (window.count.incrementAndGet() > maxRequests) {
                response.setStatus(429);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), Map.of(
                        "message", "Trop de requêtes. Réessayez dans quelques instants."
                ));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLimitedPath(String uri) {
        for (String path : LIMITED_PATHS) {
            if (uri.equals(path)) {
                return true;
            }
        }
        return false;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class Window {
        private long startEpochSecond = Instant.now().getEpochSecond();
        private final AtomicInteger count = new AtomicInteger(0);
    }
}
