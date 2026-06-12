package com.gamesUP.gamesUP.service;

import java.util.UUID;

public interface AuthorizationService {
    void checkIsAdminOrOwner(UUID userId);
}
