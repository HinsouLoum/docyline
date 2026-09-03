package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.ConcoursRequest;
import com.laboacamedy.docyline.entities.Concours;
import com.laboacamedy.docyline.service.ConcoursService;
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
public class ConcoursController {

    private final ConcoursService concoursService;

    @GetMapping
    public ResponseEntity<List<Concours>> listerTous(){
        return ResponseEntity.ok(concoursService.listerTous());
    }

    @GetMapping("/{actifs}")
    public ResponseEntity<List<Concours>> listerActifs(){
        return ResponseEntity.ok(concoursService.listerActifs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concours> obtenirParId(@PathVariable Long id){
        return ResponseEntity.ok(concoursService.obtenirParId(id));
    }

    @PostMapping
    public ResponseEntity<Concours> creer(@Valid @RequestBody ConcoursRequest requete){
        return ResponseEntity.status(HttpStatus.CREATED).body(concoursService.creer(requete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concours>modifier(@PathVariable Long id, @Valid @RequestBody ConcoursRequest requete){
        return ResponseEntity.ok(concoursService.modifier(id, requete));
    }

//    Active ou desactive un concours plutot que de le supprimer (tracabilite conservee)
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Void> changerStatut(@PathVariable Long id, @RequestParam boolean actif){
        concoursService.changerStatutConcour(id, actif);
        return ResponseEntity.noContent().build();
    }
}
