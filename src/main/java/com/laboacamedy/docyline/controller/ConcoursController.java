package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.ConcoursRequest;
import com.laboacamedy.docyline.entities.Concours;
import com.laboacamedy.docyline.service.ConcoursService;
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
 * Controleur REST de gestion des concours.
 * Consultation publique (GET) ; creation/modification reservees Admin + Gestionnaire (cf. SecurityConfig).
 */
@RestController
@RequestMapping("/api/concours")
@RequiredArgsConstructor
@Tag(
        name = "Concours",
        description = "Gestion des concours (examens) disponibles sur la plateforme. " +
                "Les opérations de lecture sont publiques, les modifications sont réservées aux administrateurs. " +
                "Note : Les concours ne sont jamais supprimés, seulement désactivés (soft delete)."
)

public class ConcoursController {

    private final ConcoursService concoursService;

    @GetMapping
    @Operation(
            summary = "Lister tous les concours",
            description = "Récupère la liste complète de tous les concours disponibles sur la plateforme, " +
                    "y compris les concours inactifs. Endpoint public, aucune authentification requise.",
            tags = {"Concours"},
            operationId = "listerTous"
    )
    public ResponseEntity<List<Concours>> listerTous(){
        return ResponseEntity.ok(concoursService.listerTous());
    }

    @GetMapping("/{actifs}")
    @Operation(
            summary = "Lister les concours actifs uniquement",
            description = "Récupère la liste de tous les concours actuellement actifs et ouverts aux candidats. " +
                    "Les concours inactifs ou fermés ne sont pas retournés. Endpoint public.",
            tags = {"Concours"},
            operationId = "listerActifs"
    )
    public ResponseEntity<List<Concours>> listerActifs(){
        return ResponseEntity.ok(concoursService.listerActifs());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un concours par son ID",
            description = "Récupère les détails complets d'un concours spécifique en fonction de son identifiant. " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Concours"},
            operationId = "obtenirParId"
    )
    public ResponseEntity<Concours> obtenirParId(@PathVariable Long id){
        return ResponseEntity.ok(concoursService.obtenirParId(id));
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouveau concours",
            description = "Permet à un administrateur ou gestionnaire de créer un nouveau concours. " +
                    "Les champs obligatoires sont 'nom', 'dateDebut' et 'dateFin'. " +
                    "L'ID et la date de création sont générés automatiquement.",
            tags = {"Concours"},
            operationId = "creer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Concours> creer(@Valid @RequestBody ConcoursRequest requete){
        return ResponseEntity.status(HttpStatus.CREATED).body(concoursService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un concours existant",
            description = "Permet à un administrateur ou gestionnaire de modifier les détails d'un concours existant. " +
                    "L'ID du concours et la date de création restent inchangés.",
            tags = {"Concours"},
            operationId = "modifier",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Concours>modifier(@PathVariable Long id, @Valid @RequestBody ConcoursRequest requete){
        return ResponseEntity.ok(concoursService.modifier(id, requete));
    }

//    Active ou desactive un concours plutot que de le supprimer (tracabilite conservee)
    @PatchMapping("/{id}/statut")
    @Operation(
            summary = "Changer le statut d'un concours (actif/inactif)",
            description = "Permet à un administrateur ou gestionnaire d'activer ou désactiver un concours. " +
                    "IMPORTANT : Cet endpoint ne supprime pas le concours mais le marque comme inactif. " +
                    "C'est une suppression logique (soft delete) qui préserve la traçabilité des données. " +
                    "Un concours inactif n'apparaît plus dans la liste des concours actifs.",
            tags = {"Concours"},
            operationId = "changerStatut",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> changerStatut(@PathVariable Long id, @RequestParam boolean actif){
        concoursService.changerStatutConcour(id, actif);
        return ResponseEntity.noContent().build();
    }
}
