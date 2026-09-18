package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** DTO pour la reponse de resultat. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultatResponse {
    private Long id;
    private Integer score;
    private Integer scoreMax;
    private Double pourcentage;
    private LocalDateTime date;
    private Long candidatId;
    private Long quizId;
}
