package com.nexora.seller.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexora.auth.dto.RegisterRequest;
import com.nexora.common.enums.SellerStatus;
import com.nexora.seller.dto.SellerApplyRequest;
import com.nexora.seller.repository.SellerRepository;
import com.nexora.shop.dto.ShopRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void applyAsSellerAndAccessDashboard() throws Exception {
        AuthTokens tokens = register("seller-flow@test.com");

        SellerApplyRequest apply = new SellerApplyRequest();
        apply.setCompanyName("Test Shop SARL");
        apply.setTaxId("FR123");

        mockMvc.perform(post("/api/sellers/apply")
                        .header("Authorization", "Bearer " + tokens.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apply)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyName", is("Test Shop SARL")))
                .andExpect(jsonPath("$.status", is("PENDING")));

        String sellerToken = refresh(tokens.refreshToken());

        mockMvc.perform(get("/api/sellers/me/dashboard")
                        .header("Authorization", "Bearer " + sellerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerStatus", is("PENDING")));
    }

    @Test
    void approvedSellerCanCreateShopAndProduct() throws Exception {
        AuthTokens tokens = register("approved-seller@test.com");

        SellerApplyRequest apply = new SellerApplyRequest();
        apply.setCompanyName("Approved Co");
        mockMvc.perform(post("/api/sellers/apply")
                        .header("Authorization", "Bearer " + tokens.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apply)))
                .andExpect(status().isCreated());

        sellerRepository.findAll().stream()
                .filter(s -> "Approved Co".equals(s.getCompanyName()))
                .findFirst()
                .ifPresent(s -> {
                    s.setStatus(SellerStatus.APPROVED);
                    sellerRepository.save(s);
                });

        String sellerToken = refresh(tokens.refreshToken());

        ShopRequest shop = new ShopRequest();
        shop.setName("Ma Boutique Test");
        shop.setDescription("Description boutique");

        mockMvc.perform(put("/api/sellers/me/shop")
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shop)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ma Boutique Test")));

        String productBody = """
                {"name":"Produit Test","description":"Desc","price":99.99,"stock":10}
                """;

        mockMvc.perform(post("/api/sellers/me/products")
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Produit Test")))
                .andExpect(jsonPath("$.stock", is(10)));
    }

    @Test
    void sellerEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/sellers/me/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    private AuthTokens register(String email) throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setEmail(email);
        register.setPassword("password123");
        register.setFirstName("Test");
        register.setLastName("Seller");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return new AuthTokens(body.get("accessToken").asText(), body.get("refreshToken").asText());
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
