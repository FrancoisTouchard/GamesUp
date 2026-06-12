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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.InventoryDTO;
import com.gamesUP.gamesUP.service.InventoryService;

class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    private InventoryDTO buildInventory() {
        InventoryDTO inventory = new InventoryDTO();
        inventory.setGameName("Test Game");
        inventory.setStock(10);
        return inventory;
    }

    @Test
    void testFindAll() throws Exception {
        when(inventoryService.findAll()).thenReturn(List.of(buildInventory()));

        mockMvc.perform(MockMvcRequestBuilders.get("/inventories/private/user"))
                .andExpect(status().isOk());

        verify(inventoryService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        InventoryDTO inventory = buildInventory();
        inventory.setId(id);

        when(inventoryService.findById(id)).thenReturn(inventory);

        mockMvc.perform(MockMvcRequestBuilders.get("/inventories/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(inventoryService).findById(id);
    }

    @Test
    void testFindByGameId() throws Exception {
        UUID gameId = UUID.randomUUID();
        when(inventoryService.findByGameId(gameId)).thenReturn(buildInventory());

        mockMvc.perform(MockMvcRequestBuilders.get("/inventories/private/user/game/" + gameId))
                .andExpect(status().isOk());

        verify(inventoryService).findByGameId(gameId);
    }

    @Test
    void testCreate() throws Exception {
        InventoryDTO inventory = buildInventory();

        when(inventoryService.create(any())).thenReturn(inventory);

        mockMvc.perform(MockMvcRequestBuilders.post("/inventories/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(inventory)))
                .andExpect(status().isCreated());

        verify(inventoryService).create(any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        InventoryDTO inventory = buildInventory();

        when(inventoryService.partialUpdate(eq(id), any())).thenReturn(inventory);

        mockMvc.perform(MockMvcRequestBuilders.patch("/inventories/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(inventory)))
                .andExpect(status().isOk());

        verify(inventoryService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/inventories/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(inventoryService).deleteById(id);
    }
}
