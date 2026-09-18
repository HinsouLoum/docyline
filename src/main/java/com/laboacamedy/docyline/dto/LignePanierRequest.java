package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/** DTO pour la creation/modification d'une ligne de panier. */
@Data
public class LignePanierRequest {

    @NotNull(message = "L'ID panier est obligatoire")
    private Long panierId;

    @NotNull(message = "L'ID document est obligatoire")
    private Long documentId;

    @Positive(message = "La quantite doit etre positive")
    private Integer quantite = 1;

    @NotNull(message = "Le prix unitaire est obligatoire")
    private BigDecimal prixUnitaire;
}
