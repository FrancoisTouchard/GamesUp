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

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.service.PurchaseLineService;

class PurchaseLineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseLineService purchaseLineService;

    @InjectMocks
    private PurchaseLineController purchaseLineController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseLineController).build();
    }

    private PurchaseLineDTO buildPurchaseLine(UUID purchaseId) {
        PurchaseLineDTO line = new PurchaseLineDTO();
        line.setPurchaseId(purchaseId);
        line.setGameName("Test Game");
        return line;
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        PurchaseLineDTO line = buildPurchaseLine(UUID.randomUUID());
        line.setId(id);

        when(purchaseLineService.findById(id)).thenReturn(line);

        mockMvc.perform(MockMvcRequestBuilders.get("/purchase-lines/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(purchaseLineService).findById(id);
    }

    @Test
    void testFindByPurchaseId() throws Exception {
        UUID purchaseId = UUID.randomUUID();
        when(purchaseLineService.findByPurchaseId(purchaseId)).thenReturn(List.of(buildPurchaseLine(purchaseId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/purchase-lines/private/user/purchase/" + purchaseId))
                .andExpect(status().isOk());

        verify(purchaseLineService).findByPurchaseId(purchaseId);
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/purchase-lines/private/user/" + id))
                .andExpect(status().isNoContent());

        verify(purchaseLineService).deleteById(id);
    }
}
