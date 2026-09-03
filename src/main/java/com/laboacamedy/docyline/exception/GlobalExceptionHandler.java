package com.laboacamedy.docyline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions de l'API.
 * Centralise la transformation des exceptions metier en reponses HTTP coherentes,
 * evitant de repeter la gestion d'erreur dans chaque controleur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RessourceNonTrouveeException.class)
    public ResponseEntity<ErreurReponse> gererRessouceNonTrouvee(RessourceNonTrouveeException e){
        return construireReponse(HttpStatus.NOT_FOUND, e.getMessage(),null);
    }

    @ExceptionHandler(RequeteInvalideException.class)
    public ResponseEntity<ErreurReponse> gererRequeteInvalideException(RequeteInvalideException e){
        return construireReponse(HttpStatus.BAD_REQUEST, e.getMessage(),null);

    }

    @ExceptionHandler({AcceRefuseException.class, AccessDeniedException.class})
    public ResponseEntity<ErreurReponse> gererAccesRefuse(RuntimeException ex) {
        return construireReponse(HttpStatus.FORBIDDEN, "Acces refuse : " + ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErreurReponse> gererIdentifiantsInvalides(BadCredentialsException ex) {
        return construireReponse(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect", null);
    }

    // Erreurs de validation des DTO (@Valid) : renvoie le detail champ par champ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurReponse> gererErreursValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();
        for (FieldError erreur : ex.getBindingResult().getFieldErrors()) {
            erreurs.put(erreur.getField(), erreur.getDefaultMessage());
        }
        return construireReponse(HttpStatus.BAD_REQUEST, "Donnees invalides", erreurs);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurReponse> gererErreurGenerique(Exception ex) {
        return construireReponse(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue est survenue : " + ex.getMessage(), null);
    }

    private ResponseEntity<ErreurReponse> construireReponse(HttpStatus statut, String message, Map<String, String> details) {
        ErreurReponse erreur = ErreurReponse.builder()
                .horodatage(LocalDateTime.now())
                .statut(statut.value())
                .message(message)
                .detail(details)
                .build();
        return ResponseEntity.status(statut).body(erreur);
    }
}
