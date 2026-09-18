package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.BibliothequeUtilisateurResponse;
import com.laboacamedy.docyline.service.BibliothequeUtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST de gestion des bibliotheques utilisateurs.
 */
@RestController
@RequestMapping("/api/bibliotheques")
@RequiredArgsConstructor
@Tag(name = "Bibliothèques", description = "Gestion des bibliothèques personnelles des utilisateurs")
public class BibliothequeUtilisateurController {

    private final BibliothequeUtilisateurService bibliothequeService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une bibliothèque par son ID",
            description = "Récupère les détails complets d'une bibliothèque spécifique",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BibliothequeUtilisateurResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(bibliothequeService.obtenirParId(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Obtenir la bibliothèque d'un utilisateur",
            description = "Récupère la bibliothèque personnelle d'un utilisateur",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BibliothequeUtilisateurResponse> obtenirParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(bibliothequeService.obtenirParUtilisateur(utilisateurId));
    }

    @PostMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Créer ou obtenir la bibliothèque d'un utilisateur",
            description = "Crée une nouvelle bibliothèque ou retourne celle existante",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BibliothequeUtilisateurResponse> creerOuObtenirPourUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bibliothequeService.creerOuObtenirPourUtilisateur(utilisateurId));
    }

    @PutMapping("/{bibliothequeId}/documents/{documentId}")
    @Operation(
            summary = "Ajouter un document à la bibliothèque",
            description = "Ajoute un document à la collection personnelle de l'utilisateur",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BibliothequeUtilisateurResponse> ajouterDocument(@PathVariable Long bibliothequeId, @PathVariable Long documentId) {
        return ResponseEntity.ok(bibliothequeService.ajouterDocument(bibliothequeId, documentId));
    }

    @DeleteMapping("/{bibliothequeId}/documents/{documentId}")
    @Operation(
            summary = "Retirer un document de la bibliothèque",
            description = "Supprime un document de la collection personnelle de l'utilisateur",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BibliothequeUtilisateurResponse> retirerDocument(@PathVariable Long bibliothequeId, @PathVariable Long documentId) {
        return ResponseEntity.ok(bibliothequeService.retirerDocument(bibliothequeId, documentId));
    }

    @GetMapping("/utilisateur/{utilisateurId}/possede/{documentId}")
    @Operation(
            summary = "Vérifier la possession d'un document",
            description = "Vérifie si un utilisateur possède un document spécifique",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Boolean> verifierPossessionDocument(@PathVariable Long utilisateurId, @PathVariable Long documentId) {
        return ResponseEntity.ok(bibliothequeService.verifierPossessionDocument(utilisateurId, documentId));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une bibliothèque",
            description = "Supprime complètement une bibliothèque",
            tags = {"Bibliothèques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        bibliothequeService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
