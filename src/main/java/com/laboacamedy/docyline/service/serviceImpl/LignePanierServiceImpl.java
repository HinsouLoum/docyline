package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.LignePanierRequest;
import com.laboacamedy.docyline.dto.LignePanierResponse;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.entities.LignePanier;
import com.laboacamedy.docyline.entities.Panier;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.DocumentRepository;
import com.laboacamedy.docyline.repository.LignePanierRepository;
import com.laboacamedy.docyline.repository.PanierRepository;
import com.laboacamedy.docyline.service.LignePanierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des lignes de panier. */
@Service
@RequiredArgsConstructor
@Transactional
public class LignePanierServiceImpl implements LignePanierService {

    private final LignePanierRepository lignePanierRepository;
    private final PanierRepository panierRepository;
    private final DocumentRepository documentRepository;

    @Override
    public LignePanierResponse ajouter(LignePanierRequest requete) {
        Panier panier = panierRepository.findById(requete.getPanierId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Panier introuvable avec l'id : " + requete.getPanierId()));

        Document document = documentRepository.findById(requete.getDocumentId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Document introuvable avec l'id : " + requete.getDocumentId()));

        // Verifier que le document n'est pas deja dans le panier
        boolean exists = panier.getLignes().stream()
                .anyMatch(l -> l.getDocument().getId().equals(requete.getDocumentId()));
        if (exists) {
            throw new RequeteInvalideException("Ce document est deja present dans le panier");
        }

        LignePanier lignePanier = LignePanier.builder()
                .panier(panier)
                .document(document)
                .quantite(requete.getQuantite())
                .prixUnitaire(requete.getPrixUnitaire())
                .build();

        LignePanier ligneSauvegardee = lignePanierRepository.save(lignePanier);
        return mapperVersResponse(ligneSauvegardee);
    }

    @Override
    public LignePanierResponse modifier(Long id, LignePanierRequest requete) {
        LignePanier lignePanier = lignePanierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Ligne panier introuvable avec l'id : " + id));

        lignePanier.setQuantite(requete.getQuantite());
        lignePanier.setPrixUnitaire(requete.getPrixUnitaire());

        LignePanier ligneModifiee = lignePanierRepository.save(lignePanier);
        return mapperVersResponse(ligneModifiee);
    }

    @Override
    @Transactional(readOnly = true)
    public LignePanierResponse obtenirParId(Long id) {
        LignePanier lignePanier = lignePanierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Ligne panier introuvable avec l'id : " + id));
        return mapperVersResponse(lignePanier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LignePanierResponse> listerParPanier(Long panierId) {
        if (!panierRepository.existsById(panierId)) {
            throw new RessourceNonTrouveeException("Panier introuvable avec l'id : " + panierId);
        }
        return lignePanierRepository.findAll().stream()
                .filter(l -> l.getPanier().getId().equals(panierId))
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimer(Long id) {
        LignePanier lignePanier = lignePanierRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Ligne panier introuvable avec l'id : " + id));
        lignePanierRepository.delete(lignePanier);
    }

    @Override
    public void supprimerParPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Panier introuvable avec l'id : " + panierId));
        lignePanierRepository.deleteAll(panier.getLignes());
    }

    private LignePanierResponse mapperVersResponse(LignePanier lignePanier) {
        return LignePanierResponse.builder()
                .id(lignePanier.getId())
                .quantite(lignePanier.getQuantite())
                .prixUnitaire(lignePanier.getPrixUnitaire())
                .panierId(lignePanier.getPanier().getId())
                .documentId(lignePanier.getDocument().getId())
                .documentTitre(lignePanier.getDocument().getTitre())
                .build();
    }
}
