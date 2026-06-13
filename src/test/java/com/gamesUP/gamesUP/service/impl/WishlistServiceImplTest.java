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

import com.gamesUP.gamesUP.dto.WishlistDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.mapper.WishlistMapper;

class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private WishlistMapper wishlistMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private Wishlist wishlist;
    private WishlistDTO wishlistDTO;
    private AppUser user;
    private UUID id;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new AppUser();
        user.setId(userId);

        wishlist = new Wishlist();
        wishlist.setId(id);
        wishlist.setUser(user);

        wishlistDTO = new WishlistDTO();
        wishlistDTO.setId(id);
        wishlistDTO.setUserId(userId);
    }

    @Test
    void testFindAll() {
        when(wishlistRepository.findAll()).thenReturn(List.of(wishlist));
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        List<WishlistDTO> result = wishlistService.findAll();

        assertEquals(1, result.size());
        assertEquals(wishlistDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        WishlistDTO result = wishlistService.findById(id);

        assertEquals(wishlistDTO, result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindById_NotFound() {
        when(wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.findById(id));
    }

    @Test
    void testFindByUserId_Found() {
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.of(wishlist));
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        WishlistDTO result = wishlistService.findByUserId(userId);

        assertEquals(wishlistDTO, result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindByUserId_NotFound() {
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.findByUserId(userId));
    }

    @Test
    void testCreate_Success() {
        when(wishlistRepository.existsByUserId(userId)).thenReturn(false);
        when(wishlistMapper.toEntity(wishlistDTO)).thenReturn(wishlist);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        WishlistDTO result = wishlistService.create(wishlistDTO);

        assertEquals(wishlistDTO, result);
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(wishlistRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> wishlistService.create(wishlistDTO));
        verify(authorizationService).checkIsAdminOrOwner(userId);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        Game game = new Game();
        game.setName("Test Game");

        WishlistDTO updateDTO = new WishlistDTO();
        updateDTO.setGameNames(List.of("Test Game"));

        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        wishlistService.update(id, updateDTO);

        assertEquals(List.of(game), wishlist.getWishlistedGames());
        verify(authorizationService).checkIsAdminOrOwner(userId);
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void testUpdate_NotFound() {
        when(wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.update(id, wishlistDTO));
    }

    @Test
    void testUpdate_GameNotFound() {
        WishlistDTO updateDTO = new WishlistDTO();
        updateDTO.setGameNames(List.of("Unknown Game"));

        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(gameRepository.findByName("Unknown Game")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.update(id, updateDTO));
    }

    @Test
    void testPartialUpdate() {
        Game game = new Game();
        game.setName("Test Game");

        WishlistDTO updateDTO = new WishlistDTO();
        updateDTO.setGameNames(List.of("Test Game"));

        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
        when(wishlistMapper.toDTO(wishlist)).thenReturn(wishlistDTO);

        wishlistService.partialUpdate(id, updateDTO);

        assertEquals(List.of(game), wishlist.getWishlistedGames());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void testDeleteById_Success() {
        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));

        wishlistService.deleteById(id);

        assertNull(user.getWishlist());
        verify(authorizationService).checkIsAdminOrOwner(userId);
        verify(appUserRepository).save(user);
        verify(wishlistRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.deleteById(id));
        verify(wishlistRepository, never()).deleteById(any());
    }
}
