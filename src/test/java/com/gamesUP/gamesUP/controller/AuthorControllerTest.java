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
import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.service.AuthorService;

class AuthorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    private AuthorDTO buildAuthor() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Test Author");
        return author;
    }

    @Test
    void testFindAll() throws Exception {
        when(authorService.findAll()).thenReturn(List.of(buildAuthor()));

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/private/user"))
                .andExpect(status().isOk());

        verify(authorService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        AuthorDTO author = buildAuthor();
        author.setId(id);

        when(authorService.findById(id)).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(authorService).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        AuthorDTO author = buildAuthor();

        when(authorService.create(any())).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.post("/authors/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(author)))
                .andExpect(status().isCreated());

        verify(authorService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        AuthorDTO author = buildAuthor();

        when(authorService.update(eq(id), any())).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.put("/authors/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(author)))
                .andExpect(status().isOk());

        verify(authorService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        AuthorDTO author = buildAuthor();

        when(authorService.partialUpdate(eq(id), any())).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(author)))
                .andExpect(status().isOk());

        verify(authorService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(authorService).deleteById(id);
    }
}
