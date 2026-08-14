package com.nexora.payment.controller;

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
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void paymentCalculatesCommissionsAndUpdatesBalance() throws Exception {
        ProductSetup setup = createSellerProduct("pay-vendor@test.com", "Pay Vendor", "Pay Product");
        AuthTokens client = register("pay-client@test.com");

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + setup.offerId() + ",\"quantity\":1}"))
                .andExpect(status().isCreated());

        long addressId = createAddress(client.accessToken());
        long orderId = createOrder(client.accessToken(), addressId);

        mockMvc.perform(post("/api/payments")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":" + orderId + ",\"method\":\"CARD\",\"cardNumber\":\"4242424242424242\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("COMPLETED")))
                .andExpect(jsonPath("$.amount", is(29.99)));

        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + client.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));

        String sellerToken = setup.sellerTokens().accessToken();

        mockMvc.perform(get("/api/sellers/me/balance")
                        .header("Authorization", "Bearer " + sellerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEarned", is(26.99)))
                .andExpect(jsonPath("$.availableBalance", is(26.99)));

        mockMvc.perform(get("/api/sellers/me/commissions")
                        .header("Authorization", "Bearer " + sellerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].commissionRate", is(10.0)))
                .andExpect(jsonPath("$[0].commissionAmount", is(3.0)))
                .andExpect(jsonPath("$[0].sellerAmount", is(26.99)));
    }

    @Test
    void sellerCanRequestWithdrawal() throws Exception {
        ProductSetup setup = createSellerProduct("withdraw-vendor@test.com", "Withdraw Vendor", "Withdraw Product");
        AuthTokens client = register("withdraw-client@test.com");

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"offerId\":" + setup.offerId() + ",\"quantity\":1}"))
                .andExpect(status().isCreated());

        long addressId = createAddress(client.accessToken());
        long orderId = createOrder(client.accessToken(), addressId);

        mockMvc.perform(post("/api/payments")
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":" + orderId + ",\"method\":\"PAYPAL\"}"))
                .andExpect(status().isCreated());

        String sellerToken = setup.sellerTokens().accessToken();

        mockMvc.perform(post("/api/sellers/me/withdrawals")
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":10.00,\"bankAccount\":\"FR7612345678901234567890123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("PENDING")));

        mockMvc.perform(get("/api/sellers/me/balance")
                        .header("Authorization", "Bearer " + sellerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableBalance", is(16.99)))
                .andExpect(jsonPath("$.totalWithdrawn", is(10.0)));
    }

    @Test
    void paymentRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"method\":\"CARD\",\"cardNumber\":\"4242424242424242\"}"))
                .andExpect(status().isUnauthorized());
    }

    private long createAddress(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/addresses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"label":"Home","street":"1 rue Test","city":"Paris","postalCode":"75001","country":"France","defaultAddress":true}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private long createOrder(String token, long addressId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"addressId\":" + addressId + "}"))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
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
