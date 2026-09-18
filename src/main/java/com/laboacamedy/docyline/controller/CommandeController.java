package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.CommandeRequest;
import com.laboacamedy.docyline.dto.CommandeResponse;
import com.laboacamedy.docyline.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST de gestion des commandes.
 *
 * Permissions :
 * - GET (lister/obtenir) : Utilisateur authentifié
 * - POST/PUT/DELETE : Réservé aux administrateurs et gestionnaires
 */
@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@Tag(name = "Commandes", description = "Gestion des commandes de documents")
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping
    @Operation(
            summary = "Lister toutes les commandes",
            description = "Récupère la liste complète de toutes les commandes",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<CommandeResponse>> listerToutes() {
        return ResponseEntity.ok(commandeService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une commande par son ID",
            description = "Récupère les détails complets d'une commande spécifique",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<CommandeResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.obtenirParId(id));
    }

    @GetMapping("/numero/{numero}")
    @Operation(
            summary = "Obtenir une commande par son numéro",
            description = "Récupère les détails complets d'une commande via son numéro unique",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<CommandeResponse> obtenirParNumero(@PathVariable String numero) {
        return ResponseEntity.ok(commandeService.obtenirParNumero(numero));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Lister les commandes d'un utilisateur",
            description = "Récupère toutes les commandes passées par un utilisateur spécifique",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<CommandeResponse>> listerParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(commandeService.listerParUtilisateur(utilisateurId));
    }

    @PostMapping
    @Operation(
            summary = "Créer une nouvelle commande",
            description = "Crée une nouvelle commande à partir d'une liste de documents",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<CommandeResponse> creer(@Valid @RequestBody CommandeRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.creer(requete));
    }

    @PutMapping("/{id}/annuler")
    @Operation(
            summary = "Annuler une commande",
            description = "Annule une commande existante",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<CommandeResponse> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.annuler(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une commande",
            description = "Supprime une commande de la base de données",
            tags = {"Commandes"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        commandeService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
