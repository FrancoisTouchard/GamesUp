package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.mapper.PublisherMapper;

class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherMapper publisherMapper;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher;
    private PublisherDTO publisherDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        publisher = new Publisher();
        publisher.setId(id);
        publisher.setName("Test Publisher");

        publisherDTO = new PublisherDTO();
        publisherDTO.setId(id);
        publisherDTO.setName("Test Publisher");
    }

    @Test
    void testFindAll() {
        when(publisherRepository.findAll()).thenReturn(List.of(publisher));
        when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

        List<PublisherDTO> result = publisherService.findAll();

        assertEquals(1, result.size());
        assertEquals(publisherDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

        PublisherDTO result = publisherService.findById(id);

        assertEquals(publisherDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> publisherService.findById(id));
    }

    @Test
    void testCreate_Success() {
        when(publisherRepository.existsByName("Test Publisher")).thenReturn(false);
        when(publisherMapper.toEntity(publisherDTO)).thenReturn(publisher);
        when(publisherRepository.save(publisher)).thenReturn(publisher);
        when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

        PublisherDTO result = publisherService.create(publisherDTO);

        assertEquals(publisherDTO, result);
        verify(publisherRepository).save(publisher);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(publisherRepository.existsByName("Test Publisher")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> publisherService.create(publisherDTO));
        verify(publisherRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        PublisherDTO updateDTO = new PublisherDTO();
        updateDTO.setName("New Publisher");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(publisher)).thenReturn(publisher);
        when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

        publisherService.update(id, updateDTO);

        assertEquals("New Publisher", publisher.getName());
        verify(publisherRepository).save(publisher);
    }

    @Test
    void testUpdate_NotFound() {
        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> publisherService.update(id, publisherDTO));
    }

    @Test
    void testPartialUpdate() {
        PublisherDTO updateDTO = new PublisherDTO();
        updateDTO.setName("New Publisher");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(publisher)).thenReturn(publisher);
        when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

        publisherService.partialUpdate(id, updateDTO);

        assertEquals("New Publisher", publisher.getName());
        verify(publisherRepository).save(publisher);
    }

    @Test
    void testDeleteById_Success() {
        Game game = new Game();
        game.setPublisher(publisher);

        when(publisherRepository.existsById(id)).thenReturn(true);
        when(gameRepository.findByPublisherId(id)).thenReturn(List.of(game));

        publisherService.deleteById(id);

        assertNull(game.getPublisher());
        verify(gameRepository).save(game);
        verify(publisherRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(publisherRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> publisherService.deleteById(id));
        verify(publisherRepository, never()).deleteById(id);
    }
}
