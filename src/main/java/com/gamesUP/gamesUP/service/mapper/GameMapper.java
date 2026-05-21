package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public GameDTO toDTO(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setAuthor(game.getAuthor());
        dto.setGenre(game.getGenre());
        dto.setNumEdition(game.getNumEdition());
        if (game.getCategory() != null) {
            dto.setCategoryName(game.getCategory().getName());
        }
        if (game.getPublisher() != null) {
            dto.setPublisherName(game.getPublisher().getName());
        }
        return dto;
    }

    public Game toEntity(GameDTO dto) {
        Game game = new Game();
        game.setName(dto.getName());
        game.setAuthor(dto.getAuthor());
        game.setGenre(dto.getGenre());
        game.setNumEdition(dto.getNumEdition());
        if (dto.getCategoryName() != null) {
            Category category = categoryRepository.findByName(dto.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + dto.getCategoryName()));
            game.setCategory(category);
        }
        if (dto.getPublisherName() != null) {
            Publisher publisher = publisherRepository.findByName(dto.getPublisherName())
                    .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable : " + dto.getPublisherName()));
            game.setPublisher(publisher);
        }
        return game;
    }
}
