package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.PurchaseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PurchaseDTO {

    private UUID id;

    @NotNull(message = "Un achat doit être associé à un utilisateur.")
    private UUID userId;

    private List<PurchaseLineDTO> purchaseLines;

    private BigDecimal totalPrice;

    private LocalDateTime date;

    @NotNull(message = "Un achat doit avoir un statut.")
    private PurchaseStatus status;
}
