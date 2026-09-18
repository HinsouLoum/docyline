package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.DashboardUtilisateurResponse;
import com.laboacamedy.docyline.dto.StatistiquesAchatResponse;
import com.laboacamedy.docyline.service.StatistiquesAchatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST de gestion des statistiques d'achat des utilisateurs.
 */
@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
@Tag(name = "Statistiques", description = "Gestion des statistiques d'achat et de consultation des utilisateurs")
public class StatistiquesAchatController {

    private final StatistiquesAchatService statistiquesService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir des statistiques par ID",
            description = "Récupère les statistiques d'achat complètes",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<StatistiquesAchatResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(statistiquesService.obtenirParId(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Obtenir les statistiques d'un utilisateur",
            description = "Récupère toutes les statistiques d'achat et de consultation d'un utilisateur",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<StatistiquesAchatResponse> obtenirParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(statistiquesService.obtenirParUtilisateur(utilisateurId));
    }

    @GetMapping("/dashboard/{utilisateurId}")
    @Operation(
            summary = "Obtenir le dashboard statistiques d'un utilisateur",
            description = "Récupère un résumé complet des statistiques sous forme de dashboard",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<DashboardUtilisateurResponse> obtenirDashboard(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(statistiquesService.obtenirDashboardUtilisateur(utilisateurId));
    }

    @GetMapping("/top/clients")
    @Operation(
            summary = "Lister les meilleurs clients (par montant dépensé)",
            description = "Récupère les N meilleurs clients par dépenses totales",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<StatistiquesAchatResponse>> obtenirTopMeilleurClients(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statistiquesService.obtenirTopMeilleurClients(limit));
    }

    @GetMapping("/top/acheteurs")
    @Operation(
            summary = "Lister les plus gros acheteurs (par quantité de documents)",
            description = "Récupère les N utilisateurs ayant acheté le plus de documents",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<StatistiquesAchatResponse>> obtenirTopPlusAcheteurs(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statistiquesService.obtenirTopPlusAcheteurs(limit));
    }

    @GetMapping("/top/telecharges")
    @Operation(
            summary = "Lister les utilisateurs avec plus de téléchargements",
            description = "Récupère les N utilisateurs ayant téléchargé le plus de documents",
            tags = {"Statistiques"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<StatistiquesAchatResponse>> obtenirTopPlusTelecharges(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statistiquesService.obtenirTopPlusTelecharges(limit));
    }
}
