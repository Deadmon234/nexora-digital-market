package com.marketplace.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.AbstractIntegrationTest;
import com.marketplace.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class AuthFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void registersClientAndHashesPassword() throws Exception {
        register("client@nexora.test", "Password123!")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.role").value("CLIENT"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());

        assertThat(userRepository.findByEmailIgnoreCase("client@nexora.test"))
                .get()
                .extracting(user -> user.getPasswordHash())
                .satisfies(hash -> assertThat(hash).isNotEqualTo("Password123!").startsWith("$2"));
    }

    @Test
    void rejectsDuplicateEmail() throws Exception {
        register("dup@nexora.test", "Password123!").andExpect(status().isCreated());
        register("dup@nexora.test", "Password123!").andExpect(status().isConflict());
    }

    @Test
    void rejectsWrongPassword() throws Exception {
        register("wrong@nexora.test", "Password123!").andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new java.util.HashMap<>(java.util.Map.of(
                                        "email", "wrong@nexora.test", "password", "NotThePassword")))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTokenRotatesAndCannotBeReused() throws Exception {
        JsonNode registration = objectMapper.readTree(
                register("rotate@nexora.test", "Password123!").andReturn().getResponse().getContentAsString());
        String refreshToken = registration.get("refreshToken").asText();

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void meReturnsAuthenticatedProfile() throws Exception {
        JsonNode registration = objectMapper.readTree(
                register("me@nexora.test", "Password123!").andReturn().getResponse().getContentAsString());

        mockMvc.perform(get("/api/v1/users/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + registration.get("accessToken").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("me@nexora.test"));
    }

    private org.springframework.test.web.servlet.ResultActions register(String email, String password)
            throws Exception {
        String payload = objectMapper.writeValueAsString(java.util.Map.of(
                "email", email,
                "password", password,
                "firstName", "Test",
                "lastName", "User",
                "phone", "+237600000000"));
        return mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));
    }
}
