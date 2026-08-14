package com.nexora.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nexora.jwt")
@Getter
@Setter
public class JwtProperties {

    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;
}
