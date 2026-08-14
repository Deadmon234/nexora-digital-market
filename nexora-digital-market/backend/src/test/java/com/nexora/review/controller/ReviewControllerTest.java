package com.nexora.review.controller;

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
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void clientCanReviewPurchasedProduct() throws Exception {
        ProductSetup setup = createSellerProduct("review-vendor@test.com", "Review Vendor", "Review Product");
        AuthTokens client = register("review-client@test.com");

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

        String productSlug = setup.productSlug();

        mockMvc.perform(get("/api/reviews/products/" + productSlug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewCount", is(0)));

        mockMvc.perform(post("/api/reviews/products/" + productSlug)
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":5,\"comment\":\"Excellent produit !\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.comment", is("Excellent produit !")));

        mockMvc.perform(get("/api/reviews/products/" + productSlug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewCount", is(1)))
                .andExpect(jsonPath("$.averageRating", is(5.0)))
                .andExpect(jsonPath("$.reviews[0].rating", is(5)));

        mockMvc.perform(post("/api/reviews/products/" + productSlug)
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":4,\"comment\":\"Doublon\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reviewRequiresPurchase() throws Exception {
        ProductSetup setup = createSellerProduct("review-vendor2@test.com", "Review Vendor 2", "Review Product 2");
        AuthTokens client = register("review-no-buy@test.com");

        mockMvc.perform(post("/api/reviews/products/" + setup.productSlug())
                        .header("Authorization", "Bearer " + client.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":3,\"comment\":\"Sans achat\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void productReviewsArePublic() throws Exception {
        mockMvc.perform(get("/api/reviews/products/unknown-product"))
                .andExpect(status().isNotFound());
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

    private ProductSetup createSellerProduct(String email, String company, String productName) throws Exception {
        AuthTokens seller = register(email);
        SellerApplyRequest apply = new SellerApplyRequest();
        apply.setCompanyName(company);
        mockMvc.perform(post("/api/sellers/apply")
                        .header("Authorization", "Bearer " + seller.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apply)))
                .andExpect(status().isCreated());

        sellerRepository.findAll().stream()
                .filter(s -> company.equals(s.getCompanyName()))
                .findFirst()
                .ifPresent(s -> {
                    s.setStatus(SellerStatus.APPROVED);
                    sellerRepository.save(s);
                });

        AuthTokens sellerTokens = refreshTokens(seller);

        MvcResult productResult = mockMvc.perform(post("/api/sellers/me/products")
                        .header("Authorization", "Bearer " + sellerTokens.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"%s","description":"Desc","price":29.99,"stock":10}
                                """.formatted(productName)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(productResult.getResponse().getContentAsString());
        return new ProductSetup(sellerTokens, body.get("offerId").asLong(), body.get("slug").asText());
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
    private record ProductSetup(AuthTokens sellerTokens, long offerId, String productSlug) {}
}
