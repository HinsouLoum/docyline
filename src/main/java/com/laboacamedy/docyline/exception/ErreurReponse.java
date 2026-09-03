package com.laboacamedy.docyline.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/** Format uniforme des reponses d'erreur renvoyees par l'API. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErreurReponse {
    private LocalDateTime horodatage;
    private int statut;
    private String message;
    private Map<String, String> detail; // Utilise notament pour les erreurs de validation
}
