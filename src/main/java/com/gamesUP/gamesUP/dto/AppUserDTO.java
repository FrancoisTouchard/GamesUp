package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AppUserDTO {

    private UUID id;

    @NotBlank(message = "Un utilisateur doit avoir un nom.")
    private String name;

    @NotBlank(message = "Un utilisateur doit avoir un mot de passe.")
    private String password;

    private UUID wishlistId;
}
