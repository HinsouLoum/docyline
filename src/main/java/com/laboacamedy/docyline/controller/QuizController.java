package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.QuizRequest;
import com.laboacamedy.docyline.dto.QuizResponse;
import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import com.laboacamedy.docyline.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controleur REST de gestion des quiz.
 * Creation/modification/suppression reservees Admin + Gestionnaire (cf. SecurityConfig, pattern "/api/quiz/**").
 * Consultation (GET) : utilisateurs authentifies (candidats en preparation).
 */
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // GET /api/quiz : liste complete (usage gestion/back-office)
    @GetMapping
    public ResponseEntity<List<QuizResponse>> listerTous() {
        return ResponseEntity.ok(quizService.listerTous());
    }

    // GET /api/quiz/publies : uniquement les quiz publies (usage candidat)
    @GetMapping("/publies")
    public ResponseEntity<List<QuizResponse>> listerPublies() {
        return ResponseEntity.ok(quizService.listerPublies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un quiz", description = "Récupère les détails d'un quiz par ID")
    @ApiResponse(responseCode = "200", description = "Quiz trouvé")
    @ApiResponse(responseCode = "404", description = "Quiz non trouvé")
    public ResponseEntity<QuizResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.obtenirParId(id));
    }

    @GetMapping("/filtrer/concours/{concoursId}")
    public ResponseEntity<List<QuizResponse>> listerParConcours(@PathVariable Long concoursId) {
        return ResponseEntity.ok(quizService.listerParConcours(concoursId));
    }

    @GetMapping("/filtrer/matiere/{matiereId}")
    public ResponseEntity<List<QuizResponse>> listerParMatiere(@PathVariable Long matiereId) {
        return ResponseEntity.ok(quizService.listerParMatiere(matiereId));
    }

    // POST /api/quiz : creation d'un quiz complet (titre + questions + reponses en une seule requete)
    @PostMapping
    @Operation(summary = "Créer un quiz", description = "Crée un nouveau quiz avec questions et réponses")
    @ApiResponse(responseCode = "201", description = "Quiz créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<QuizResponse> creer(@Valid @RequestBody QuizRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un quiz")
    public ResponseEntity<QuizResponse> modifier(@PathVariable Long id, @Valid @RequestBody QuizRequest requete) {
        return ResponseEntity.ok(quizService.modifier(id, requete));
    }

    // Publie le quiz (le rend visible/passable par les candidats)
    @PatchMapping("/{id}/publier")
    public ResponseEntity<Void> publier(@PathVariable Long id) {
        quizService.changerStatut(id, StatutQuiz.PUBLIE);
        return ResponseEntity.noContent().build();
    }

    // Archive le quiz (le retire de la liste des quiz disponibles sans le supprimer)
    @PatchMapping("/{id}/archiver")
    public ResponseEntity<Void> archiver(@PathVariable Long id) {
        quizService.changerStatut(id, StatutQuiz.ARCHIVE);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un quiz")
    @ApiResponse(responseCode = "204", description = "Quiz supprimé")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        quizService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
