package com.gamesUP.gamesUP.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.service.AppUserService;
import com.gamesUP.gamesUP.service.JwtService;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserService appUserService;

    @Mock
    private JwtService jwtService;

    private AuthController authController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(jwtService, appUserService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john", "password");
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/public/login")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));

        verify(jwtService).generateToken(any());
    }

    @Test
    void testSignup() throws Exception {
        AppUserDTO dto = new AppUserDTO();
        dto.setName("john");
        dto.setPassword("password");

        when(appUserService.register(any())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/public/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(appUserService).register(any());
    }

    @Test
    void testLogoutWithToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/private/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer fake-jwt-token"))
                .andExpect(status().isNoContent());

        verify(jwtService).blacklist("fake-jwt-token");
    }

    @Test
    void testLogoutWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/private/user/logout"))
                .andExpect(status().isNoContent());

        verify(jwtService, never()).blacklist(any());
    }
}
