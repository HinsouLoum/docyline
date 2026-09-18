package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** DTO pour la reponse de ligne de panier. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LignePanierResponse {
    private Long id;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private Long panierId;
    private Long documentId;
    private String documentTitre;

    public BigDecimal getMontantLigne() {
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

}
