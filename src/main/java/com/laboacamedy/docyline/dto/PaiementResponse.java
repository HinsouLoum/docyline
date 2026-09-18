package com.laboacamedy.docyline.dto;

import com.laboacamedy.docyline.entities.enums.ModePaiement;
import com.laboacamedy.docyline.entities.enums.StatutPaiement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO pour la reponse de paiement. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementResponse {

    private Long id;
    private BigDecimal montant;
    private String reference;
    private ModePaiement mode;
    private LocalDateTime date;
    private StatutPaiement statut;
    private Long commandeId;
}
