package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO pour la reponse de reponse. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReponseResponse {
    private Long id;
    private String contenu;
    private Boolean estCorrecte;
    private Long questionId;
}
