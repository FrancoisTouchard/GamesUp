package com.gamesUP.gamesUP.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.service.GameService;

class GameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    private GameDTO buildGame() {
        GameDTO game = new GameDTO();
        game.setName("Test Game");
        game.setPublisherName("Test Publisher");
        game.setNumEdition(1);
        game.setPrice(BigDecimal.valueOf(59.99));
        return game;
    }

    @Test
    void testFindAll() throws Exception {
        when(gameService.findAll()).thenReturn(List.of(buildGame()));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user"))
                .andExpect(status().isOk());

        verify(gameService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        GameDTO game = buildGame();
        game.setId(id);

        when(gameService.findById(id)).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(gameService).findById(id);
    }

    @Test
    void testFindByCategoryName() throws Exception {
        when(gameService.findByCategoryName("Action")).thenReturn(List.of(buildGame()));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user/category/Action"))
                .andExpect(status().isOk());

        verify(gameService).findByCategoryName("Action");
    }

    @Test
    void testFindByGenreName() throws Exception {
        when(gameService.findByGenreName("RPG")).thenReturn(List.of(buildGame()));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user/genre/RPG"))
                .andExpect(status().isOk());

        verify(gameService).findByGenreName("RPG");
    }

    @Test
    void testFindByPublisherName() throws Exception {
        when(gameService.findByPublisherName("Test Publisher")).thenReturn(List.of(buildGame()));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user/publisher/Test Publisher"))
                .andExpect(status().isOk());

        verify(gameService).findByPublisherName("Test Publisher");
    }

    @Test
    void testFindByAuthorName() throws Exception {
        when(gameService.findByAuthorName("Test Author")).thenReturn(List.of(buildGame()));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/private/user/author/Test Author"))
                .andExpect(status().isOk());

        verify(gameService).findByAuthorName("Test Author");
    }

    @Test
    void testCreate() throws Exception {
        GameDTO game = buildGame();

        when(gameService.create(any())).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.post("/games/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(game)))
                .andExpect(status().isCreated());

        verify(gameService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        GameDTO game = buildGame();

        when(gameService.update(eq(id), any())).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.put("/games/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(game)))
                .andExpect(status().isOk());

        verify(gameService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        GameDTO game = buildGame();

        when(gameService.partialUpdate(eq(id), any())).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.patch("/games/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(game)))
                .andExpect(status().isOk());

        verify(gameService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/games/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(gameService).deleteById(id);
    }
}
