package com.laboacamedy.docyline.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** DTO d'une question, imbriquee dans la creation d'un quiz, avec ses propositions de reponses. */
@Data
public class QuestionRequest {

    @NotBlank(message = "Le contenu de la question est obligatoire")
    private String contenu;

    @NotNull(message = "Le nombre de points est obligatoire")
    private Integer points;

    @NotEmpty(message = "Une question doit avoir au moins une proposition de reponse")
    @Valid
    private List<ReponseRequest> reponses;
}
