package com.nexora.order.controller;

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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void createOrderFromCartWithSubOrders() throws Exception {
        long offer1 = createSellerProduct("order-vendor-a@test.com", "Order Vendor A", "Order Product A").offerId();
        long offer2 = createSellerProduct("order-vendor-b@test.com", "Order Vendor B", "Order Product B").offerId();

        AuthTokens client = register("order-client@test.com");

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

        MvcResult addressResult = mockMvc.perform(post("/api/addresses")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"label":"Maison","street":"1 rue Test","city":"Paris","postalCode":"75001","country":"France","defaultAddress":true}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        long addressId = objectMapper.readTree(addressResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"addressId\":" + addressId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.orderNumber", not(emptyString())))
                .andExpect(jsonPath("$.sellerOrders", hasSize(2)))
                .andExpect(jsonPath("$.itemCount", is(3)))
                .andReturn();

        long orderId = objectMapper.readTree(orderResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount", is(0)));

        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerOrders", hasSize(2)));
    }

    @Test
    void sellerCanUpdateOrderStatus() throws Exception {
        ProductSetup setup = createSellerProduct("status-vendor@test.com", "Status Vendor", "Status Product");
        AuthTokens client = register("status-client@test.com");

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + setup.offerId() + ",\"quantity\":1}"))
                .andExpect(status().isCreated());

        MvcResult addressResult = mockMvc.perform(post("/api/addresses")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"label":"Home","street":"1 st","city":"Lyon","postalCode":"69001","country":"France","defaultAddress":true}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        long addressId = objectMapper.readTree(addressResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"addressId\":" + addressId + "}"))
                .andExpect(status().isCreated())
                .andReturn();

        long orderId = objectMapper.readTree(orderResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(post("/api/payments")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":" + orderId + ",\"method\":\"CARD\",\"cardNumber\":\"4242424242424242\"}"))
                .andExpect(status().isCreated());

        long sellerOrderId = objectMapper.readTree(orderResult.getResponse().getContentAsString())
                .get("sellerOrders").get(0).get("id").asLong();

        String sellerToken = setup.sellerTokens().accessToken();

        mockMvc.perform(patch("/api/sellers/me/orders/" + sellerOrderId + "/status")
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PROCESSING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PROCESSING")));
    }

    @Test
    void createOrderRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"addressId\":1}"))
                .andExpect(status().isUnauthorized());
    }

    private ProductSetup createSellerProduct(String email, String companyName, String productName) throws Exception {
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

        AuthTokens sellerTokens = refreshTokens(seller);
        String token = sellerTokens.accessToken();

        MvcResult product = mockMvc.perform(post("/api/sellers/me/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"%s","description":"Desc","price":29.99,"stock":10}
                                """.formatted(productName)))
                .andExpect(status().isCreated())
                .andReturn();

        long offerId = objectMapper.readTree(product.getResponse().getContentAsString()).get("offerId").asLong();
        return new ProductSetup(offerId, sellerTokens);
    }

    private AuthTokens register(String email) throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setEmail(email);
        register.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return new AuthTokens(body.get("accessToken").asText(), body.get("refreshToken").asText());
    }

    private AuthTokens refreshTokens(AuthTokens tokens) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + tokens.refreshToken() + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return new AuthTokens(body.get("accessToken").asText(), body.get("refreshToken").asText());
    }

    private record AuthTokens(String accessToken, String refreshToken) {}
    private record ProductSetup(long offerId, AuthTokens sellerTokens) {}
}
