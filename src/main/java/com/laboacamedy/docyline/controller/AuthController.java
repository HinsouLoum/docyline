package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.AuthResponse;
import com.laboacamedy.docyline.dto.ConnexionRequest;
import com.laboacamedy.docyline.dto.InscriptionResquest;
import com.laboacamedy.docyline.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST gérant l'inscription et la connexion des utilisateurs.
 * Routes publiques (cf. SecurityConfig : /api/auth/** est accessible sans authentification).
 *
 * Endpoints disponibles :
 * - POST /api/auth/inscription : Créer un nouveau compte candidat
 * - POST /api/auth/connexion : Authentifier l'utilisateur et obtenir le token JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'inscription et la connexion des utilisateurs")
public class AuthController {

    private final AuthService authService;

    /**
     * Créer un nouveau compte utilisateur (inscription).
     *
     * @param requete les données d'inscription de l'utilisateur
     * @return une réponse contenant le token JWT et les informations de l'utilisateur
     */
//    POST / API/auth/inscription : creation d'un compte candidat
    @PostMapping("/inscription")
    @Operation(
            summary = "Créer un nouveau compte utilisateur",
            description = "Permet à un nouvel utilisateur de s'inscrire en fournissant ses informations personnelles. " +
                    "Un compte est créé et un token JWT est généré pour l'authentification.",
            tags = {"Authentification"}
    )
    public ResponseEntity<AuthResponse> inscrire(@Valid @RequestBody InscriptionResquest requete){
        AuthResponse reponse = authService.inscrire(requete);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

//    POST /api/auth/connexion : authentification et recuperation du token JWT
    @PostMapping("/connexion")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Permet à un utilisateur enregistré de se connecter en fournissant ses identifiants. " +
                    "Si les identifiants sont corrects, un token JWT est généré et retourné pour les requêtes suivantes.",
            tags = {"Authentification"}
    )
    public ResponseEntity<AuthResponse> connecter(@Valid @RequestBody ConnexionRequest requete){
        AuthResponse response = authService.connecter(requete);
        return ResponseEntity.ok(response);
    }
}
