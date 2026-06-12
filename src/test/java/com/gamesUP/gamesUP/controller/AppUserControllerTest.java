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
import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.service.AppUserService;

class AppUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private AppUserController appUserController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();
    }

    private AppUserDTO buildUser() {
        AppUserDTO user = new AppUserDTO();
        user.setName("john");
        user.setPassword("password");
        return user;
    }

    @Test
    void testFindAll() throws Exception {
        when(appUserService.findAll()).thenReturn(List.of(buildUser()));

        mockMvc.perform(MockMvcRequestBuilders.get("/private/admin/users"))
                .andExpect(status().isOk());

        verify(appUserService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        AppUserDTO user = buildUser();
        user.setId(id);

        when(appUserService.findById(id)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/private/admin/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(appUserService).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        AppUserDTO user = buildUser();

        when(appUserService.create(any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/private/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(appUserService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        AppUserDTO user = buildUser();

        when(appUserService.update(eq(id), any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/private/admin/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(appUserService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        AppUserDTO user = buildUser();

        when(appUserService.partialUpdate(eq(id), any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/private/admin/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(appUserService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/private/admin/users/" + id))
                .andExpect(status().isNoContent());

        verify(appUserService).deleteById(id);
    }
}
