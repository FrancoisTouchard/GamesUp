package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUserDTO toDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
//        dto.setPassword(user.getPassword());
        if (user.getWishlist() != null) {
            dto.setWishlistId(user.getWishlist().getId());
        }
        return dto;
    }

    public AppUser toEntity(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        return user;
    }
}
