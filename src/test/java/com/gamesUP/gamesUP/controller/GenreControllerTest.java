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
import com.gamesUP.gamesUP.dto.GenreDTO;
import com.gamesUP.gamesUP.service.GenreService;

class GenreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    private GenreDTO buildGenre() {
        GenreDTO genre = new GenreDTO();
        genre.setName("RPG");
        return genre;
    }

    @Test
    void testFindAll() throws Exception {
        when(genreService.findAll()).thenReturn(List.of(buildGenre()));

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/private/user"))
                .andExpect(status().isOk());

        verify(genreService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        GenreDTO genre = buildGenre();
        genre.setId(id);

        when(genreService.findById(id)).thenReturn(genre);

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(genreService).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        GenreDTO genre = buildGenre();

        when(genreService.create(any())).thenReturn(genre);

        mockMvc.perform(MockMvcRequestBuilders.post("/genres/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(genre)))
                .andExpect(status().isCreated());

        verify(genreService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        GenreDTO genre = buildGenre();

        when(genreService.update(eq(id), any())).thenReturn(genre);

        mockMvc.perform(MockMvcRequestBuilders.put("/genres/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(genre)))
                .andExpect(status().isOk());

        verify(genreService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        GenreDTO genre = buildGenre();

        when(genreService.partialUpdate(eq(id), any())).thenReturn(genre);

        mockMvc.perform(MockMvcRequestBuilders.patch("/genres/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(genre)))
                .andExpect(status().isOk());

        verify(genreService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/genres/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(genreService).deleteById(id);
    }
}
