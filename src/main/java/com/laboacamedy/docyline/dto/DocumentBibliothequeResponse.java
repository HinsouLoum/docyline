package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO pour les documents dans la bibliotheque de l'utilisateur. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentBibliothequeResponse {

    private Long id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private LocalDateTime dateAcquisition;
    private Integer nombreTelechargements;
    private LocalDateTime dernierTelechargement;
}
