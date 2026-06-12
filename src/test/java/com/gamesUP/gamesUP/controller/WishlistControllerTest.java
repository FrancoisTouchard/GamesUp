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
import com.gamesUP.gamesUP.dto.WishlistDTO;
import com.gamesUP.gamesUP.service.WishlistService;

class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).build();
    }

    private WishlistDTO buildWishlist(UUID userId) {
        WishlistDTO wishlist = new WishlistDTO();
        wishlist.setUserId(userId);
        return wishlist;
    }

    @Test
    void testFindAll() throws Exception {
        when(wishlistService.findAll()).thenReturn(List.of(buildWishlist(UUID.randomUUID())));

        mockMvc.perform(MockMvcRequestBuilders.get("/wishlists/private/admin"))
                .andExpect(status().isOk());

        verify(wishlistService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        WishlistDTO wishlist = buildWishlist(UUID.randomUUID());
        wishlist.setId(id);

        when(wishlistService.findById(id)).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.get("/wishlists/private/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(wishlistService).findById(id);
    }

    @Test
    void testFindByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        WishlistDTO wishlist = buildWishlist(userId);

        when(wishlistService.findByUserId(userId)).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.get("/wishlists/private/user/userWishList/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(wishlistService).findByUserId(userId);
    }

    @Test
    void testCreate() throws Exception {
        WishlistDTO wishlist = buildWishlist(UUID.randomUUID());

        when(wishlistService.create(any())).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlists/private/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wishlist)))
                .andExpect(status().isCreated());

        verify(wishlistService).create(any());
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        WishlistDTO wishlist = buildWishlist(UUID.randomUUID());

        when(wishlistService.update(eq(id), any())).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.put("/wishlists/private/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wishlist)))
                .andExpect(status().isOk());

        verify(wishlistService).update(eq(id), any());
    }

    @Test
    void testPartialUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        WishlistDTO wishlist = buildWishlist(UUID.randomUUID());

        when(wishlistService.partialUpdate(eq(id), any())).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.patch("/wishlists/private/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wishlist)))
                .andExpect(status().isOk());

        verify(wishlistService).partialUpdate(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/wishlists/private/user/" + id))
                .andExpect(status().isNoContent());

        verify(wishlistService).deleteById(id);
    }
}
