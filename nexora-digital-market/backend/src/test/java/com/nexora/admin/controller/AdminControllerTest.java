package com.nexora.admin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexora.auth.dto.LoginRequest;
import com.nexora.auth.dto.RegisterRequest;
import com.nexora.common.enums.RoleName;
import com.nexora.common.enums.SellerStatus;
import com.nexora.seller.dto.SellerApplyRequest;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.user.entity.Role;
import com.nexora.user.entity.User;
import com.nexora.user.repository.RoleRepository;
import com.nexora.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void adminCanApproveSellerAndViewAnalytics() throws Exception {
        String adminToken = createAdmin("admin-approve@test.com");
        registerSeller("pending-vendor@test.com", "Pending Vendor Co");

        mockMvc.perform(get("/api/admin/sellers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        long sellerId = sellerRepository.findAll().stream()
                .filter(s -> "Pending Vendor Co".equals(s.getCompanyName()))
                .findFirst()
                .orElseThrow()
                .getId();

        mockMvc.perform(patch("/api/admin/sellers/" + sellerId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));

        mockMvc.perform(get("/api/admin/analytics")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSellers", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalUsers", greaterThanOrEqualTo(2)));
    }

    @Test
    void adminCanUpdateCommissionRate() throws Exception {
        String adminToken = createAdmin("admin-commission@test.com");
        registerSeller("commission-vendor@test.com", "Commission Vendor");

        long sellerId = sellerRepository.findAll().stream()
                .filter(s -> "Commission Vendor".equals(s.getCompanyName()))
                .findFirst()
                .orElseThrow()
                .getId();

        sellerRepository.findById(sellerId).ifPresent(s -> {
            s.setStatus(SellerStatus.APPROVED);
            sellerRepository.save(s);
        });

        mockMvc.perform(patch("/api/admin/sellers/" + sellerId + "/commission-rate")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commissionRate\":15.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commissionRate", is(15.0)));
    }

    @Test
    void adminEndpointsRejectNonAdmin() throws Exception {
        AuthTokens client = register("client-only@test.com");

        mockMvc.perform(get("/api/admin/sellers")
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanManageCategories() throws Exception {
        String adminToken = createAdmin("admin-catalog@test.com");

        mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Accessoires\",\"slug\":\"accessoires-admin\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Accessoires")));

        mockMvc.perform(get("/api/admin/categories")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Accessoires")));
    }

    private String createAdmin(String email) {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
        Role clientRole = roleRepository.findByName(RoleName.ROLE_CLIENT).orElseThrow();
        userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .firstName("Admin")
                .lastName("Test")
                .enabled(true)
                .roles(Set.of(adminRole, clientRole))
                .build());

        try {
            return login(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String registerSeller(String email, String companyName) throws Exception {
        AuthTokens tokens = register(email);
        SellerApplyRequest apply = new SellerApplyRequest();
        apply.setCompanyName(companyName);
        apply.setTaxId("TAX-" + System.nanoTime());

        mockMvc.perform(post("/api/sellers/apply")
                        .header("Authorization", "Bearer " + tokens.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apply)))
                .andExpect(status().isCreated());

        return refresh(tokens.refreshToken());
    }

    private AuthTokens register(String email) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return new AuthTokens(body.get("accessToken").asText(), body.get("refreshToken").asText());
    }

    private String login(String email) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private String refresh(String refreshToken) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private record AuthTokens(String accessToken, String refreshToken) {}
}
