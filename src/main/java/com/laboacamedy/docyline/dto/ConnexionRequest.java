package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO recu lors de la connexion d'un utilisateur. */
@Data
public class ConnexionRequest {

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
