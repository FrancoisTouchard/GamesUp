package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PublisherMapper {

    @Autowired
    private GameMapper gameMapper;

    public PublisherDTO toDTO(Publisher publisher) {
        PublisherDTO dto = new PublisherDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        if (publisher.getGames() != null) {
            dto.setGames(publisher.getGames().stream()
                    .map(gameMapper::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setGames(Collections.emptyList());
        }
        return dto;
    }

    public Publisher toEntity(PublisherDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        return publisher;
    }
}
