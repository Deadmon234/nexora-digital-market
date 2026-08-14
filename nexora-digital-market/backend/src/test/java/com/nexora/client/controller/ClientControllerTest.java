package com.nexora.client.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexora.auth.dto.RegisterRequest;
import com.nexora.common.enums.SellerStatus;
import com.nexora.seller.dto.SellerApplyRequest;
import com.nexora.seller.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void cartMultiVendor() throws Exception {
        long offer1 = createSellerProduct("vendor-a@test.com", "Vendor A", "Product A");
        long offer2 = createSellerProduct("vendor-b@test.com", "Vendor B", "Product B");

        AuthTokens client = register("client-cart@test.com");

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + offer1 + ",\"quantity\":1}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + offer2 + ",\"quantity\":2}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.itemCount", is(3)))
                .andExpect(jsonPath("$.items[*].sellerName", containsInAnyOrder("Vendor A", "Vendor B")));
    }

    @Test
    void favoritesToggle() throws Exception {
        long offerId = createSellerProduct("vendor-fav@test.com", "Fav Vendor", "Fav Product");
        AuthTokens client = register("client-fav@test.com");

        MvcResult cartItem = mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + offerId + ",\"quantity\":1}"))
                .andExpect(status().isCreated())
                .andReturn();

        long productId = objectMapper.readTree(cartItem.getResponse().getContentAsString())
                .get("items").get(0).get("productId").asLong();

        mockMvc.perform(post("/api/favorites/" + productId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is((int) productId)));

        mockMvc.perform(get("/api/favorites/check/" + productId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited", is(true)));

        mockMvc.perform(delete("/api/favorites/" + productId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void addressCrud() throws Exception {
        AuthTokens client = register("client-addr@test.com");

        MvcResult created = mockMvc.perform(post("/api/addresses")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"label":"Maison","street":"1 rue Test","city":"Paris","postalCode":"75001","country":"France","defaultAddress":true}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city", is("Paris")))
                .andReturn();

        long addressId = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/addresses")
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(put("/api/addresses/" + addressId)
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"label":"Bureau","street":"2 ave Test","city":"Lyon","postalCode":"69001","country":"France","defaultAddress":true}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is("Bureau")));

        mockMvc.perform(delete("/api/addresses/" + addressId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isNoContent());
    }

    private long createSellerProduct(String email, String companyName, String productName) throws Exception {
        AuthTokens seller = register(email);

        SellerApplyRequest apply = new SellerApplyRequest();
        apply.setCompanyName(companyName);
        mockMvc.perform(post("/api/sellers/apply")
                        .header("Authorization", "Bearer " + seller.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apply)))
                .andExpect(status().isCreated());

        sellerRepository.findAll().stream()
                .filter(s -> companyName.equals(s.getCompanyName()))
                .findFirst()
                .ifPresent(s -> {
                    s.setStatus(SellerStatus.APPROVED);
                    sellerRepository.save(s);
                });

        String token = refresh(seller.refreshToken());

        MvcResult product = mockMvc.perform(post("/api/sellers/me/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"%s","description":"Desc","price":49.99,"stock":10}
                                """.formatted(productName)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(product.getResponse().getContentAsString()).get("offerId").asLong();
    }

    private AuthTokens register(String email) throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setEmail(email);
        register.setPassword("password123");
        register.setFirstName("Test");
        register.setLastName("User");

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
