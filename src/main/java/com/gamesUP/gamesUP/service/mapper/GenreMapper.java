package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.GenreDTO;
import com.gamesUP.gamesUP.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public GenreDTO toDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());

        return dto;
    }

    public Genre toEntity(GenreDTO dto) {
        Genre genre = new Genre();
        genre.setName(dto.getName());

        return genre;
    }
}
