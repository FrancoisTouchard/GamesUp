package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.AuthorDTO;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    List<AuthorDTO> findAll();
    AuthorDTO findById(UUID id);
    AuthorDTO create(AuthorDTO author);
    AuthorDTO update(UUID id, AuthorDTO author);
    AuthorDTO partialUpdate(UUID id, AuthorDTO author);
    void deleteById(UUID id);
}
