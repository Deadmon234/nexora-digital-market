package com.marketplace.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.marketplace.AbstractIntegrationTest;
import com.marketplace.user.entity.Role;
import com.marketplace.user.entity.User;
import com.marketplace.user.repository.UserRepository;
import com.marketplace.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class RoleAccessIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Test
    void clientCannotReachAdminArea() throws Exception {
        mockMvc.perform(get("/api/v1/admin/vendors").header(HttpHeaders.AUTHORIZATION, bearerFor(Role.CLIENT)))
                .andExpect(status().isForbidden());
    }

    @Test
    void clientCannotReachSellerArea() throws Exception {
        mockMvc.perform(get("/api/v1/seller/profile").header(HttpHeaders.AUTHORIZATION, bearerFor(Role.CLIENT)))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminReachesAdminArea() throws Exception {
        mockMvc.perform(get("/api/v1/admin/vendors").header(HttpHeaders.AUTHORIZATION, bearerFor(Role.ADMIN)))
                .andExpect(status().isOk());
    }

    private String bearerFor(Role role) {
        String email = role.name().toLowerCase() + "-" + System.nanoTime() + "@nexora.test";
        User user = userRepository.save(new User(email, passwordEncoder.encode("Password123!"), role,
                "Test", "User", null));
        return "Bearer " + jwtService.generateAccessToken(user);
    }
}
