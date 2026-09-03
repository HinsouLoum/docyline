package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.AuthResponse;
import com.laboacamedy.docyline.dto.ConnexionRequest;
import com.laboacamedy.docyline.dto.InscriptionResquest;
import com.laboacamedy.docyline.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controleur REST gerant l'inscription et la connexion des utilisateurs.
 * Routes publiques (cf. SecurityConfig : /api/auth/** est accessible sans authentification).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    POST / API/auth/inscription : creation d'un compte candidat
    @PostMapping("/inscription")
    public ResponseEntity<AuthResponse> inscrire(@Valid @RequestBody InscriptionResquest requete){
        AuthResponse reponse = authService.inscrire(requete);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

//    POST /api/auth/connexion : authentification et recuperation du token JWT
    @PostMapping("/connexion")
    public ResponseEntity<AuthResponse> connecter(@Valid @RequestBody ConnexionRequest requete){
        AuthResponse response = authService.connecter(requete);
        return ResponseEntity.ok(response);
    }
}
