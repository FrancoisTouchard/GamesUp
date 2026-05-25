package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WishlistDTO {

    private UUID id;

    @NotNull(message = "Une wishlist doit être associée à un utilisateur.")
    private UUID userId;

    private List<String> gameNames;
}
