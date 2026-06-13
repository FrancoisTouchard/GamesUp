package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.repository.AppUserRepository;

class AuthorizationServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, "password"));
    }

    private AppUser buildUser(UUID id, String roleName) {
        Role role = new Role();
        role.setName(roleName);

        AppUser user = new AppUser();
        user.setId(id);
        user.setRole(role);
        return user;
    }

    @Test
    void testCheckIsAdminOrOwner_Admin_Allowed() {
        UUID targetUserId = UUID.randomUUID();
        AppUser admin = buildUser(UUID.randomUUID(), "ADMIN");

        authenticateAs("admin");
        when(appUserRepository.findByName("admin")).thenReturn(Optional.of(admin));

        assertDoesNotThrow(() -> authorizationService.checkIsAdminOrOwner(targetUserId));
    }

    @Test
    void testCheckIsAdminOrOwner_Owner_Allowed() {
        UUID userId = UUID.randomUUID();
        AppUser owner = buildUser(userId, "USER");

        authenticateAs("john");
        when(appUserRepository.findByName("john")).thenReturn(Optional.of(owner));

        assertDoesNotThrow(() -> authorizationService.checkIsAdminOrOwner(userId));
    }

    @Test
    void testCheckIsAdminOrOwner_NeitherAdminNorOwner_Denied() {
        UUID targetUserId = UUID.randomUUID();
        AppUser otherUser = buildUser(UUID.randomUUID(), "USER");

        authenticateAs("john");
        when(appUserRepository.findByName("john")).thenReturn(Optional.of(otherUser));

        assertThrows(AccessDeniedException.class, () -> authorizationService.checkIsAdminOrOwner(targetUserId));
    }

    @Test
    void testCheckIsAdminOrOwner_UserNotFound() {
        authenticateAs("unknown");
        when(appUserRepository.findByName("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authorizationService.checkIsAdminOrOwner(UUID.randomUUID()));
    }
}
