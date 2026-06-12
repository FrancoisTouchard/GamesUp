package com.gamesUP.gamesUP.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.model.PurchaseStatus;
import com.gamesUP.gamesUP.service.PurchaseService;

class PurchaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseController).build();
    }

    private PurchaseDTO buildPurchase(UUID userId) {
        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setUserId(userId);
        purchase.setStatus(PurchaseStatus.PENDING);
        return purchase;
    }

    @Test
    void testFindAll() throws Exception {
        when(purchaseService.findAll()).thenReturn(List.of(buildPurchase(UUID.randomUUID())));

        mockMvc.perform(MockMvcRequestBuilders.get("/purchases/private/admin"))
                .andExpect(status().isOk());

        verify(purchaseService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        PurchaseDTO purchase = buildPurchase(UUID.randomUUID());
        purchase.setId(id);

        when(purchaseService.findById(id)).thenReturn(purchase);

        mockMvc.perform(MockMvcRequestBuilders.get("/purchases/private/admin/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(purchaseService).findById(id);
    }

    @Test
    void testFindByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        when(purchaseService.findByUserId(userId)).thenReturn(List.of(buildPurchase(userId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/purchases/private/user/" + userId))
                .andExpect(status().isOk());

        verify(purchaseService).findByUserId(userId);
    }

    @Test
    void testFindPendingByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        when(purchaseService.findPendingByUserId(userId)).thenReturn(buildPurchase(userId));

        mockMvc.perform(MockMvcRequestBuilders.get("/purchases/private/user/" + userId + "/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(purchaseService).findPendingByUserId(userId);
    }

    @Test
    void testAddGameToCart() throws Exception {
        UUID userId = UUID.randomUUID();
        when(purchaseService.addGameToCart(userId, "Test Game")).thenReturn(buildPurchase(userId));

        mockMvc.perform(MockMvcRequestBuilders.post("/purchases/private/user/" + userId + "/cart")
                .param("gameName", "Test Game"))
                .andExpect(status().isCreated());

        verify(purchaseService).addGameToCart(userId, "Test Game");
    }

    @Test
    void testUpdateStatus() throws Exception {
        UUID id = UUID.randomUUID();
        PurchaseDTO purchase = buildPurchase(UUID.randomUUID());
        purchase.setStatus(PurchaseStatus.PAID);

        when(purchaseService.updateStatus(id, PurchaseStatus.PAID)).thenReturn(purchase);

        mockMvc.perform(MockMvcRequestBuilders.patch("/purchases/private/admin/" + id + "/status")
                .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(purchaseService).updateStatus(id, PurchaseStatus.PAID);
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/purchases/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(purchaseService).deleteById(id);
    }
}
