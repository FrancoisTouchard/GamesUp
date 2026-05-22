package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.PublisherService;
import com.gamesUP.gamesUP.service.mapper.PublisherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherMapper publisherMapper;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<PublisherDTO> findAll() {
        return publisherRepository.findAll().stream()
                .map(publisherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherDTO findById(UUID id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
    }

    @Override
    @Transactional
    public PublisherDTO create(PublisherDTO publisher) {
        if (publisherRepository.existsByName(publisher.getName())) {
            throw new ResourceAlreadyExistsException("Un éditeur avec ce nom existe déjà : " + publisher.getName());
        }
        Publisher publisherToSave = publisherMapper.toEntity(publisher);
        return publisherMapper.toDTO(publisherRepository.save(publisherToSave));
    }

    @Override
    @Transactional
    public PublisherDTO update(UUID id, PublisherDTO publisher) {
        Publisher existing = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
        if (publisher.getName() != null) existing.setName(publisher.getName());
        return publisherMapper.toDTO(publisherRepository.save(existing));
    }

    @Override
    @Transactional
    public PublisherDTO partialUpdate(UUID id, PublisherDTO publisher) {
        Publisher existing = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
        if (publisher.getName() != null) existing.setName(publisher.getName());
        return publisherMapper.toDTO(publisherRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!publisherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Éditeur introuvable");
        }
        for (Game game : gameRepository.findByPublisherId(id)) {
            game.setPublisher(null);
            gameRepository.save(game);
        }
        publisherRepository.deleteById(id);
    }
}
