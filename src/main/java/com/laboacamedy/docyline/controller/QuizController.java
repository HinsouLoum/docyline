package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.QuizRequest;
import com.laboacamedy.docyline.dto.QuizResponse;
import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import com.laboacamedy.docyline.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Contrôleur REST de gestion des quiz.
 *
 * Permissions :
 * - GET /api/quiz (tous) : Administrateurs et gestionnaires uniquement (back-office)
 * - GET /api/quiz/publies : Utilisateurs authentifiés (candidats)
 * - GET /api/quiz/{id} : Utilisateurs authentifiés
 * - GET /api/quiz/filtrer/* : Utilisateurs authentifiés
 * - POST/PUT/PATCH/DELETE : Réservé aux administrateurs et gestionnaires
 *
 * Les quiz sont des questionnaires avec questions à choix multiples, associés à des concours et matières.
 * Les statuts des quiz : BROUILLON, PUBLIE, ARCHIVE.
 */
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Tag(
        name = "Quiz",
        description = "Gestion des quiz (questionnaires d'entraînement). " +
                "Les administrateurs créent et gèrent les quiz. " +
                "Les candidats authentifiés peuvent consulter et passer les quiz publiés. " +
                "Les quiz contiennent des questions à choix multiples avec réponses et explications."
)
public class QuizController {

    private final QuizService quizService;

    // GET /api/quiz : liste complete (usage gestion/back-office)
    @GetMapping
    @Operation(
            summary = "Lister tous les quiz (back-office)",
            description = "Récupère la liste COMPLETE de tous les quiz, y compris les brouillons et archivés. " +
                    "⚠️ Endpoint réservé au back-office/administration pour la gestion des quiz. " +
                    "Les candidats ne doivent voir que /api/quiz/publies.",
            tags = {"Quiz"},
            operationId = "listerTous",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<QuizResponse>> listerTous() {
        return ResponseEntity.ok(quizService.listerTous());
    }

    // GET /api/quiz/publies : uniquement les quiz publies (usage candidat)
    @GetMapping("/publies")
    @Operation(
            summary = "Lister les quiz publiés (pour candidats)",
            description = "Récupère la liste des quiz avec le statut PUBLIE uniquement. " +
                    "C'est la liste que les candidats voient pour s'entraîner. " +
                    "Les brouillons et archives ne sont pas retournés. " +
                    "Endpoint accessible aux utilisateurs authentifiés.",
            tags = {"Quiz"},
            operationId = "listerPublies"
    )
    public ResponseEntity<List<QuizResponse>> listerPublies() {
        return ResponseEntity.ok(quizService.listerPublies());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un quiz par son ID",
            description = "Récupère les détails complets d'un quiz spécifique, y compris toutes ses questions et options de réponse. " +
                    "Endpoint accessible aux utilisateurs authentifiés.",
            tags = {"Quiz"},
            operationId = "obtenirParId"
    )
    public ResponseEntity<QuizResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.obtenirParId(id));
    }

    @GetMapping("/filtrer/concours/{concoursId}")
    @Operation(
            summary = "Lister les quiz d'un concours",
            description = "Récupère tous les quiz publiés associés à un concours spécifique. " +
                    "Exemple : tous les quiz de préparation au BEPC 2024. " +
                    "Endpoint accessible aux utilisateurs authentifiés.",
            tags = {"Quiz"},
            operationId = "listerParConcours"
    )
    public ResponseEntity<List<QuizResponse>> listerParConcours(@PathVariable Long concoursId) {
        return ResponseEntity.ok(quizService.listerParConcours(concoursId));
    }

    @GetMapping("/filtrer/matiere/{matiereId}")
    @Operation(
            summary = "Lister les quiz d'une matière",
            description = "Récupère tous les quiz publiés associés à une matière spécifique. " +
                    "Exemple : tous les quiz de Mathématiques. " +
                    "Endpoint accessible aux utilisateurs authentifiés.",
            tags = {"Quiz"},
            operationId = "listerParMatiere"
    )
    public ResponseEntity<List<QuizResponse>> listerParMatiere(@PathVariable Long matiereId) {
        return ResponseEntity.ok(quizService.listerParMatiere(matiereId));
    }

    // POST /api/quiz : creation d'un quiz complet (titre + questions + reponses en une seule requete)
    @PostMapping
    @Operation(
            summary = "Créer un nouveau quiz",
            description = "Permet à un administrateur ou gestionnaire de créer un nouveau quiz complet " +
                    "avec questions à choix multiples et réponses. " +
                    "Le quiz est créé avec le statut BROUILLON par défaut (non visible aux candidats). " +
                    "Il faut le publier (PATCH /api/quiz/{id}/publier) pour qu'il soit accessible.",
            tags = {"Quiz"},
            operationId = "creer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<QuizResponse> creer(@Valid @RequestBody QuizRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un quiz existant",
            description = "Permet à un administrateur ou gestionnaire de modifier les détails et questions d'un quiz existant. " +
                    "L'ID et la date de création restent inchangés.",
            tags = {"Quiz"},
            operationId = "modifier",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<QuizResponse> modifier(@PathVariable Long id, @Valid @RequestBody QuizRequest requete) {
        return ResponseEntity.ok(quizService.modifier(id, requete));
    }

    // Publie le quiz (le rend visible/passable par les candidats)
    @PatchMapping("/{id}/publier")
    @Operation(
            summary = "Publier un quiz",
            description = "Change le statut d'un quiz de BROUILLON à PUBLIE, le rendant visible et accessible aux candidats. " +
                    "Une fois publié, le quiz apparaît dans GET /api/quiz/publies et peut être consulté par les candidats. " +
                    "Endpoint réservé aux administrateurs.",
            tags = {"Quiz"},
            operationId = "publier",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> publier(@PathVariable Long id) {
        quizService.changerStatut(id, StatutQuiz.PUBLIE);
        return ResponseEntity.noContent().build();
    }

    // Archive le quiz (le retire de la liste des quiz disponibles sans le supprimer)
    @PatchMapping("/{id}/archiver")
    @Operation(
            summary = "Archiver un quiz",
            description = "Change le statut d'un quiz à ARCHIVE, le retirant de la liste des quiz disponibles pour les candidats. " +
                    "Le quiz reste en base de données (soft delete) et peut être consulté directement par ID. " +
                    "Les résultats des candidats sur ce quiz restent tracés. " +
                    "Endpoint réservé aux administrateurs.",
            tags = {"Quiz"},
            operationId = "archiver",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> archiver(@PathVariable Long id) {
        quizService.changerStatut(id, StatutQuiz.ARCHIVE);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un quiz",
            description = "Supprime définitivement un quiz de la base de données. " +
                    "⚠️ ATTENTION : C'est une suppression physique (hard delete). " +
                    "Les questions, réponses et résultats associés seront supprimés. " +
                    "Préférez archiver le quiz (PATCH /api/quiz/{id}/archiver) pour une suppression logique. " +
                    "Endpoint réservé aux administrateurs.",
            tags = {"Quiz"},
            operationId = "supprimer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        quizService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
