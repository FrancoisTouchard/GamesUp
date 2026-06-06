package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.RoleRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.AppUserService;
import com.gamesUP.gamesUP.service.mapper.AppUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<AppUserDTO> findAll() {
        return appUserRepository.findAll().stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppUserDTO findById(UUID id) {
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );
        return new User(user.getName(), user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public AppUserDTO register(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
        user.setRole(defaultRole);
        return appUserMapper.toDTO(appUserRepository.save(user));
    }

    @Override
    @Transactional
    public AppUserDTO create(AppUserDTO user) {
        AppUser userToSave = appUserMapper.toEntity(user);
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserMapper.toDTO(appUserRepository.save(userToSave));
    }

    @Override
    @Transactional
    public AppUserDTO update(UUID id, AppUserDTO user) {
        AppUser existing = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        if (user.getName() != null) existing.setName(user.getName());
        if (user.getPassword() != null) existing.setPassword(user.getPassword());
        return appUserMapper.toDTO(appUserRepository.save(existing));
    }

    @Override
    @Transactional
    public AppUserDTO partialUpdate(UUID id, AppUserDTO user) {
        AppUser existing = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        if (user.getName() != null) existing.setName(user.getName());
        if (user.getPassword() != null) existing.setPassword(user.getPassword());
        return appUserMapper.toDTO(appUserRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!appUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }

        purchaseRepository.deleteAll(purchaseRepository.findByUserId(id));
        wishlistRepository.findByUserId(id).ifPresent(w -> wishlistRepository.delete(w));

        appUserRepository.deleteById(id);
    }
}
