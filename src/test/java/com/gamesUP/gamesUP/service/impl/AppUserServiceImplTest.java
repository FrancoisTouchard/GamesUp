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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.RoleRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.mapper.AppUserMapper;

class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser user;
    private AppUserDTO userDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        user = new AppUser();
        user.setId(id);
        user.setName("john");
        user.setPassword("encoded-password");

        userDTO = new AppUserDTO();
        userDTO.setId(id);
        userDTO.setName("john");
        userDTO.setPassword("password");
    }

    @Test
    void testFindAll() {
        when(appUserRepository.findAll()).thenReturn(List.of(user));
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        List<AppUserDTO> result = appUserService.findAll();

        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(appUserRepository.findById(id)).thenReturn(Optional.of(user));
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        AppUserDTO result = appUserService.findById(id);

        assertEquals(userDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(appUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appUserService.findById(id));
    }

    @Test
    void testLoadUserByUsername_Found() {
        Role role = new Role();
        role.setName("ADMIN");
        user.setRole(role);

        when(appUserRepository.findByName("john")).thenReturn(Optional.of(user));

        UserDetails result = appUserService.loadUserByUsername("john");

        assertEquals("john", result.getUsername());
        assertEquals("encoded-password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(appUserRepository.findByName("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername("unknown"));
    }

    @Test
    void testRegister() {
        Role userRole = new Role();
        userRole.setName("USER");

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        AppUserDTO result = appUserService.register(userDTO);

        assertEquals(userDTO, result);
        verify(appUserRepository).save(argThat(saved ->
                saved.getName().equals("john")
                        && saved.getPassword().equals("encoded-password")
                        && saved.getRole().equals(userRole)));
    }

    @Test
    void testRegister_RoleNotFound() {
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appUserService.register(userDTO));
    }

    @Test
    void testCreate() {
        when(appUserMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(appUserRepository.save(user)).thenReturn(user);
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        AppUserDTO result = appUserService.create(userDTO);

        assertEquals(userDTO, result);
        assertEquals("encoded-password", user.getPassword());
        verify(appUserRepository).save(user);
    }

    @Test
    void testUpdate() {
        AppUserDTO updateDTO = new AppUserDTO();
        updateDTO.setName("new-name");
        updateDTO.setPassword("new-password");

        when(appUserRepository.findById(id)).thenReturn(Optional.of(user));
        when(appUserRepository.save(user)).thenReturn(user);
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        appUserService.update(id, updateDTO);

        assertEquals("new-name", user.getName());
        assertEquals("new-password", user.getPassword());
        verify(appUserRepository).save(user);
    }

    @Test
    void testUpdate_NotFound() {
        when(appUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appUserService.update(id, userDTO));
    }

    @Test
    void testPartialUpdate() {
        AppUserDTO updateDTO = new AppUserDTO();
        updateDTO.setName("new-name");

        when(appUserRepository.findById(id)).thenReturn(Optional.of(user));
        when(appUserRepository.save(user)).thenReturn(user);
        when(appUserMapper.toDTO(user)).thenReturn(userDTO);

        appUserService.partialUpdate(id, updateDTO);

        assertEquals("new-name", user.getName());
        verify(appUserRepository).save(user);
    }

    @Test
    void testDeleteById_Success() {
        Purchase purchase = new Purchase();
        Wishlist wishlist = new Wishlist();

        when(appUserRepository.existsById(id)).thenReturn(true);
        when(purchaseRepository.findByUserId(id)).thenReturn(List.of(purchase));
        when(wishlistRepository.findByUserId(id)).thenReturn(Optional.of(wishlist));

        appUserService.deleteById(id);

        verify(purchaseRepository).deleteAll(List.of(purchase));
        verify(wishlistRepository).delete(wishlist);
        verify(appUserRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(appUserRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> appUserService.deleteById(id));
        verify(appUserRepository, never()).deleteById(id);
    }
}
