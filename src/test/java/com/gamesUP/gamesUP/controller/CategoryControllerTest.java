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
import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.service.CategoryService;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    private CategoryDTO buildCategory() {
        CategoryDTO category = new CategoryDTO();
        category.setName("Action");
        return category;
    }

    @Test
    void testFindAll() throws Exception {
        when(categoryService.findAll()).thenReturn(List.of(buildCategory()));

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/private/user"))
                .andExpect(status().isOk());

        verify(categoryService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        CategoryDTO category = buildCategory();
        category.setId(id);

        when(categoryService.findById(id)).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(categoryService).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        CategoryDTO category = buildCategory();

        when(categoryService.create(any())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isCreated());

        verify(categoryService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        CategoryDTO category = buildCategory();

        when(categoryService.update(eq(id), any())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk());

        verify(categoryService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        CategoryDTO category = buildCategory();

        when(categoryService.partialUpdate(eq(id), any())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.patch("/categories/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk());

        verify(categoryService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(id);
    }
}
