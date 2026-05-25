package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.UUID;

public interface AppUserService extends UserDetailsService {
    List<AppUserDTO> findAll();
    AppUserDTO findById(UUID id);
    AppUserDTO create(AppUserDTO user);
    AppUserDTO update(UUID id, AppUserDTO user);
    AppUserDTO partialUpdate(UUID id, AppUserDTO user);
    void deleteById(UUID id);
    AppUserDTO register(AppUserDTO user);
}
