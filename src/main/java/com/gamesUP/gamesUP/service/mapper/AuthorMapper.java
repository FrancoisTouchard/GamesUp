package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    @Autowired
    private GameMapper gameMapper;

    public AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        if (author.getGames() != null) {
            dto.setGames(author.getGames().stream()
                    .map(gameMapper::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setGames(Collections.emptyList());
        }
        return dto;
    }

    public Author toEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        return author;
    }
}
