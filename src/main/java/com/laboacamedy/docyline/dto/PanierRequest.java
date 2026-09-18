package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** DTO pour la creation/modification d'un panier. */
@Data
public class PanierRequest {
    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long utilisateurId;
}
