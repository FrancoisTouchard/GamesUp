package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PurchaseLineDTO {

    private UUID id;

    @NotNull(message = "Une ligne de commande doit être associée à une commande.")
    private UUID purchaseId;

    @NotBlank(message = "Une ligne de commande doit être associée à un jeu.")
    private String gameName;
}
