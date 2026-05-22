package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.PublisherDTO;

import java.util.List;
import java.util.UUID;

public interface PublisherService {
    List<PublisherDTO> findAll();
    PublisherDTO findById(UUID id);
    PublisherDTO create(PublisherDTO publisher);
    PublisherDTO update(UUID id, PublisherDTO publisher);
    PublisherDTO partialUpdate(UUID id, PublisherDTO publisher);
    void deleteById(UUID id);
}
