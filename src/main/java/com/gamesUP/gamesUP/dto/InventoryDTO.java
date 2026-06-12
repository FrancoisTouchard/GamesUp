package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InventoryDTO {

    private UUID id;

    @NotBlank(message = "Une entrée d'inventaire doit être associée à un jeu.")
    private String gameName;

    @PositiveOrZero(message = "Une entrée d'inventaire ne peut pas être négative.")
    private int stock;

}
