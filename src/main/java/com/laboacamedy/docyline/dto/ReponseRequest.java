package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO d'une proposition de reponse, imbriquee dans la creation d'une question. */
@Data
public class ReponseRequest {

    @NotBlank(message = "Le contenu de la reponse est obligatoire")
    private String contenu;

    // Indique si cette proposition est la (une des) bonne(s) reponse(s)
    private Boolean estCorrecte = false;
}
