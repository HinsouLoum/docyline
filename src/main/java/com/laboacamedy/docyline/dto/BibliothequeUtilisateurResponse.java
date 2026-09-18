package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/** DTO pour la reponse de bibliotheque utilisateur. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BibliothequeUtilisateurResponse {

    private Long id;
    private Long utilisateurId;
    private LocalDateTime dateCreation;
    private List<DocumentBibliothequeResponse> documents;
    private Integer nombreDocuments;
}
