package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.LignePanierResponse;
import com.laboacamedy.docyline.dto.PanierRequest;
import com.laboacamedy.docyline.dto.PanierResponse;
import com.laboacamedy.docyline.entities.LignePanier;
import com.laboacamedy.docyline.entities.Panier;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.entities.enums.StatutPanier;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.LignePanierRepository;
import com.laboacamedy.docyline.repository.PanierRepository;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.service.PanierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des paniers. */
@Service
@RequiredArgsConstructor
@Transactional
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;
    private final LignePanierRepository lignePanierRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public PanierResponse creer(PanierRequest requete) {
        Utilisateur utilisateur = utilisateurRepository.findById(requete.getUtilisateurId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + requete.getUtilisateurId()));

        Panier panier = Panier.builder()
                .utilisateur(utilisateur)
                .statut(StatutPanier.EN_COURS)
                .build();

        Panier panierSauvegarde = panierRepository.save(panier);
        return mapperVersResponse(panierSauvegarde);
    }

    @Override
    @Transactional(readOnly = true)
    public PanierResponse obtenirParId(Long id) {
        Panier panier = panierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Panier introuvable avec l'id : " + id));
        return mapperVersResponse(panier);
    }

    @Override
    @Transactional(readOnly = true)
    public PanierResponse obtenirPanierActuelUtilisateur(Long utilisateurId) {
        Panier panier = panierRepository.findByUtilisateurIdAndStatut(utilisateurId, StatutPanier.EN_COURS)
                .orElseThrow(() -> new RessourceNonTrouveeException("Aucun panier actif pour l'utilisateur : " + utilisateurId));
        return mapperVersResponse(panier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanierResponse> listerParUtilisateur(Long utilisateurId) {
        if (!utilisateurRepository.existsById(utilisateurId)) {
            throw new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId);
        }
        return panierRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanierResponse> listerToutes() {
        return panierRepository.findAll().stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void vider(Long id) {
        Panier panier = panierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Panier introuvable avec l'id : " + id));
        lignePanierRepository.deleteAll(panier.getLignes());
        panier.getLignes().clear();
        panierRepository.save(panier);
    }

    @Override
    public void supprimer(Long id) {
        Panier panier = panierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Panier introuvable avec l'id : " + id));
        panierRepository.delete(panier);
    }

    private PanierResponse mapperVersResponse(Panier panier) {
        List<LignePanierResponse> lignes = panier.getLignes().stream()
                .map(this::mapperLignePanierVersResponse)
                .collect(Collectors.toList());

        return PanierResponse.builder()
                .id(panier.getId())
                .dateCreation(panier.getDateCreation())
                .statut(panier.getStatut())
                .utilisateurId(panier.getUtilisateur().getId())
                .lignes(lignes)
                .build();
    }

    private LignePanierResponse mapperLignePanierVersResponse(LignePanier ligne) {
        return LignePanierResponse.builder()
                .id(ligne.getId())
                .quantite(ligne.getQuantite())
                .prixUnitaire(ligne.getPrixUnitaire())
                .panierId(ligne.getPanier().getId())
                .documentId(ligne.getDocument().getId())
                .documentTitre(ligne.getDocument().getTitre())
                .build();
    }
}
