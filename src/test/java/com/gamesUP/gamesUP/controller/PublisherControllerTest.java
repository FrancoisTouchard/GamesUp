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
import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.service.PublisherService;

class PublisherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController).build();
    }

    private PublisherDTO buildPublisher() {
        PublisherDTO publisher = new PublisherDTO();
        publisher.setName("Test Publisher");
        return publisher;
    }

    @Test
    void testFindAll() throws Exception {
        when(publisherService.findAll()).thenReturn(List.of(buildPublisher()));

        mockMvc.perform(MockMvcRequestBuilders.get("/publishers/private/user"))
                .andExpect(status().isOk());

        verify(publisherService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        PublisherDTO publisher = buildPublisher();
        publisher.setId(id);

        when(publisherService.findById(id)).thenReturn(publisher);

        mockMvc.perform(MockMvcRequestBuilders.get("/publishers/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(publisherService).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        PublisherDTO publisher = buildPublisher();

        when(publisherService.create(any())).thenReturn(publisher);

        mockMvc.perform(MockMvcRequestBuilders.post("/publishers/private/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(publisher)))
                .andExpect(status().isCreated());

        verify(publisherService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        PublisherDTO publisher = buildPublisher();

        when(publisherService.update(eq(id), any())).thenReturn(publisher);

        mockMvc.perform(MockMvcRequestBuilders.put("/publishers/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(publisher)))
                .andExpect(status().isOk());

        verify(publisherService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        PublisherDTO publisher = buildPublisher();

        when(publisherService.partialUpdate(eq(id), any())).thenReturn(publisher);

        mockMvc.perform(MockMvcRequestBuilders.patch("/publishers/private/admin/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(publisher)))
                .andExpect(status().isOk());

        verify(publisherService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/publishers/private/admin/" + id))
                .andExpect(status().isNoContent());

        verify(publisherService).deleteById(id);
    }
}
