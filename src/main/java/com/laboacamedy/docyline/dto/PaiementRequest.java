package com.laboacamedy.docyline.dto;

import com.laboacamedy.docyline.entities.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/** DTO pour la creation/modification d'un paiement. */
@Data
public class PaiementRequest {
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit etre positif")
    private BigDecimal montant;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement mode;

    @NotNull(message = "L'ID commande est obligatoire")
    private Long commandeId;
}
