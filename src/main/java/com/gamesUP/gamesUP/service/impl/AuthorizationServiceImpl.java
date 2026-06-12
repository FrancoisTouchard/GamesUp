package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public void checkIsAdminOrOwner(UUID userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));

        boolean isAdmin = currentUser.getRole().getName().equals(ADMIN_ROLE);
        boolean isOwner = currentUser.getId().equals(userId);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à cette ressource.");
        }
    }
}
