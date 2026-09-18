package com.laboacamedy.docyline.dto;

import com.laboacamedy.docyline.entities.enums.StatutPanier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/** DTO pour la reponse de panier. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanierResponse {
    private Long id;
    private LocalDateTime dateCreation;
    private StatutPanier statut;
    private Long utilisateurId;
    private List<LignePanierResponse> lignes;
}
