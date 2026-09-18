package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** DTO pour la creation/modification d'une commande. */
@Data
public class CommandeRequest {

    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long utilisateurId;

    @NotNull(message = "La liste des documents est obligatoire")
    private List<Long> documentIds;
}
