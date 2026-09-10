package com.laboacamedy.docyline.dto;

import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** DTO renvoye au frontend : evite d'exposer directement les entites JPA (et leurs cycles de relations). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {

    private Long id;
    private String titre;
    private String description;
    private Integer dureeMinutes;
    private StatutQuiz statut;
    private String nomConcours;
    private String nomMatiere;
    private Integer nombreQuestions;
    private List<QuestionResponse> questions; // rempli uniquement pour le detail (obtenirParId)

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResponse {
        private Long id;
        private String contenu;
        private Integer points;
        private List<ReponseResponse> reponses;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReponseResponse {
        private Long id;
        private String contenu;
        // Note : "estCorrecte" n'est expose ici que cote gestion (creation/consultation admin).
        // Pour le passage de quiz cote candidat, prevoir un DTO separe SANS ce champ (a ne jamais reveler avant correction).
        private Boolean estCorrecte;
    }
}
