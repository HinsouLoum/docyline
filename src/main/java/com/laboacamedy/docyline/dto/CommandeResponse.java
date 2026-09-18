package com.laboacamedy.docyline.dto;

import com.laboacamedy.docyline.entities.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** DTO pour la reponse de commande. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeResponse {

    private Long id;
    private String numero;
    private LocalDateTime dateCommande;
    private BigDecimal montantTotal;
    private StatutCommande statut;
    private Long utilisateurId;
    private List<Long> documentIds;
}
